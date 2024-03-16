package com.example.companyservice.client;

import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.client.dto.response.TicketResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ticket-service")
public interface TicketServiceClient {

    @GetMapping("/api/v1/tickets/{centerId}")
    BaseResponseDto<List<TicketResponseDto>> getTicketList(@PathVariable long centerId);

    @GetMapping("/api/v1/reviews/count/{centerId}")
    BaseResponseDto<Integer> getReviewCountByCenterId(@PathVariable long centerId);
}
