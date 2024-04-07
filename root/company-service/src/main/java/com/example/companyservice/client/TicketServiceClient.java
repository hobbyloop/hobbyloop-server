package com.example.companyservice.client;

import com.example.companyservice.client.dto.response.BookmarkScoreTicketResponseDto;
import com.example.companyservice.client.dto.response.BookmarkTicketResponseDto;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.client.dto.response.TicketResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ticket-service")
public interface TicketServiceClient {

    @GetMapping("/api/v1/tickets/{centerId}")
    BaseResponseDto<List<TicketResponseDto>> getTicketList(@PathVariable(value = "centerId") long centerId);

    @PostMapping("/api/v1/tickets/bookmark-center")
    BaseResponseDto<Map<Long, BookmarkScoreTicketResponseDto>> getBookmarkTicketList(@RequestBody List<Long> centerIdList);

    @GetMapping("/api/v1/reviews/count/{centerId}")
    BaseResponseDto<Integer> getReviewCountByCenterId(@PathVariable(value = "centerId") long centerId);
}
