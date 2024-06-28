package com.example.companyservice.company.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "광고 등록 요청 바디")
public class AdvertisementRequestDto {

    @NotBlank
    @Schema(description = "광고명", required = true)
    private String campaignName;

    @NotBlank
    @Schema(description = "사업자 번호", example = "CPC/CPM/배너광고", required = true)
    private String adType;

    @Schema(description = "한달 노출 횟수", required = true)
    private int postingCount;

    @Schema(description = "한달 클릭 횟수", required = true)
    private int clickCount;

    @Max(value = 25)
    @Min(value = 0)
    @Schema(description = "배너광고 게재 순위", required = true)
    private int adRank;

    @Schema(description = "입찰가", required = true)
    private int price;

    @Schema(description = "광고 문구", required = true)
    private String content;

    @Schema(description = "키워드", required = true)
    private String keyword;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "광고 시작 기간", example = "2024-01-01", required = true)
    private LocalDate adStart;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "광고 종료 기간", example = "2024-01-01", required = true)
    private LocalDate adEnd;

    @NotNull
    @Schema(description = "광고 금액", required = true)
    private int adPrice;

    @NotNull
    @Schema(description = "할인 금액", required = true)
    private int discountPrice;

    @NotNull
    @Schema(description = "부가세", required = true)
    private int vat;

    @NotNull
    @Schema(description = "총 금액", required = true)
    private int totalPrice;

    @NotBlank
    @Schema(description = "결제 방법", example = "선결제 후불결제", required = true)
    private String paymentType;

    @NotBlank
    @Schema(description = "은행 이름", example = "농협", required = true)
    private String bankName;

    @NotBlank
    @Schema(description = "계좌번호", required = true)
    private String accountNumber;
}
