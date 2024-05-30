package com.example.ticketservice.ticket.repository.review;

import com.example.ticketservice.ticket.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> getReviewList(long ticketId, long reviewId);

    List<Review> getReviewListSorting(long ticketId, int pageNo, int sortId);

    List<Review> getReviewListByCenterSorting(long centerId, int pageNo, int sortId);
}
