package com.example.ticketservice.repository.review;

import com.example.ticketservice.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> getReviewList(long ticketId, long reviewId);

    List<Review> getReviewListSorting(long ticketId, int pageNo, int sortId);
}
