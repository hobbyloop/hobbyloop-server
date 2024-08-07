package com.example.ticketservice.pay.toss;

import com.example.ticketservice.pay.dto.request.PaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.request.TossPaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.exception.PSPConfirmationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class TossPaymentClient {
    @Value("${payment.toss.url}")
    private String BASE_URL;

    @Value("${payment.toss.secret-key}")
    private String SECRET_KEY;

    private final String CONFIRM_URI = "/v1/payments/confirm";

    private final String TRANSACTION_URI = "/v1/transactions";

    private WebClient webClient;

    @PostConstruct
    public void init() {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedKey = encoder.encode((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + encoder.encodeToString(encodedKey);

        webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Authorization", authorizations)
                .build();
    }

    public Mono<PaymentConfirmExecuteResponseDto> executeConfirm(PaymentConfirmRequestDto requestDto) {

        TossPaymentConfirmRequestDto tossPaymentConfirmRequestDto = TossPaymentConfirmRequestDto.from(requestDto);

        return webClient.post()
                .uri(CONFIRM_URI)
                .header("Idempotency-Key", requestDto.getIdempotencyKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(tossPaymentConfirmRequestDto))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return response.bodyToMono(TossPaymentConfirmResponseDto.TossFailureResponseDto.class)
                            .flatMap(error -> {
                                TossPaymentException tossException = TossPaymentException.get(error.getCode());
                                return Mono.error(PSPConfirmationException.from(tossException));
                            });
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    return Mono.error(new RuntimeException("Toss payment 5xx error"));
                })
                .bodyToMono(TossPaymentConfirmResponseDto.class)
                .map(PaymentConfirmExecuteResponseDto::from)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                        .jitter(0.1)
                        .filter(throwable -> (throwable instanceof PSPConfirmationException && ((PSPConfirmationException) throwable).isRetryableError())
                                || throwable instanceof TimeoutException)
                        .onRetryExhaustedThrow((retries, retrySignal) -> retrySignal.failure()));

    }

    public Mono<PaymentConfirmExecuteResponseDto> executeFullCancel(String paymentKey,
                                                                String idempotencyKey,
                                                                    String cancelReason) {

        return webClient.post()
                .uri("/v1/payments/" + paymentKey + "/cancel")
                .header("Idempotency-Key", idempotencyKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("cancelReason", cancelReason)))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return response.bodyToMono(TossPaymentConfirmResponseDto.TossFailureResponseDto.class)
                            .flatMap(error -> {
                                TossPaymentException tossException = TossPaymentException.get(error.getCode());
                                return Mono.error(PSPConfirmationException.from(tossException));
                            });
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    return Mono.error(new RuntimeException("Toss payment 5xx error"));
                })
                .bodyToMono(TossPaymentConfirmResponseDto.class)
                .map(PaymentConfirmExecuteResponseDto::from)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                        .jitter(0.1)
                        .filter(throwable -> (throwable instanceof PSPConfirmationException && ((PSPConfirmationException) throwable).isRetryableError())
                                || throwable instanceof TimeoutException)
                        .onRetryExhaustedThrow((retries, retrySignal) -> retrySignal.failure()));
    }

    public Mono<PaymentConfirmExecuteResponseDto> executePartialCancel(String paymentKey,
                                                                    String idempotencyKey,
                                                                    String cancelReason,
                                                                       Long cancelAmount) {

        return webClient.post()
                .uri("/v1/payments/" + paymentKey + "/cancel")
                .header("Idempotency-Key", idempotencyKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("cancelReason", cancelReason, "cancelAmount", cancelAmount)))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return response.bodyToMono(TossPaymentConfirmResponseDto.TossFailureResponseDto.class)
                            .flatMap(error -> {
                                TossPaymentException tossException = TossPaymentException.get(error.getCode());
                                return Mono.error(PSPConfirmationException.from(tossException));
                            });
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    return Mono.error(new RuntimeException("Toss payment 5xx error"));
                })
                .bodyToMono(TossPaymentConfirmResponseDto.class)
                .map(PaymentConfirmExecuteResponseDto::from)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                        .jitter(0.1)
                        .filter(throwable -> (throwable instanceof PSPConfirmationException && ((PSPConfirmationException) throwable).isRetryableError())
                                || throwable instanceof TimeoutException)
                        .onRetryExhaustedThrow((retries, retrySignal) -> retrySignal.failure()));
    }

    // TODO: 재시도 로직 추가
    public List<TossTransactionsResponseDto> executeTransaction(LocalDateTime startDate, LocalDateTime endDate, String startingAfter) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSACTION_URI)
                        .queryParam("startDate", startDate)
                        .queryParam("endDate", endDate)
                        .build())
                .retrieve()
                .bodyToFlux(TossTransactionsResponseDto.class)
                .collectList()
                .block();
    }
}
