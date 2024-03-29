package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.dto.request.ReviewRequestDto;
import com.example.ticketservice.dto.response.ReviewCommentResponseDto;
import com.example.ticketservice.dto.response.ReviewResponseDto;
import com.example.ticketservice.entity.Comment;
import com.example.ticketservice.entity.Review;
import com.example.ticketservice.entity.ReviewImage;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.*;
import com.example.ticketservice.repository.review.ReviewRepository;
import com.example.ticketservice.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final TicketRepository ticketRepository;

    private final ReviewRepository reviewRepository;

    private final CommentRepository commentRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final ReviewImageRepository reviewImageRepository;

    private final AmazonS3Service amazonS3Service;

    @Override
    @Transactional
    public Long createReview(long memberId, long ticketId, ReviewRequestDto requestDto, List<MultipartFile> reviewImageList) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        Review review = Review.of(requestDto, ticket, memberId, "", ticket.getCenterId());
        saveReviewImage(review, ticket, reviewImageList);
        Review saveReview = reviewRepository.save(review);
        return saveReview.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewCommentResponseDto> getAdminReviewList(long ticketId, long reviewId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        return getReviewCommentResponseDtoList(ticket.getId(), reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getReviewCountByCenterId(long centerId) {
        return reviewRepository.countByCenterId(centerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewList(long memberId, long ticketId, int pageNo, int sortId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        List<Review> reviewList = reviewRepository.getReviewListSorting(ticketId, pageNo, sortId);
        return reviewList
                .stream()
                .map(r -> ReviewResponseDto.of(r,
                        ticket.getName(),
                        reviewImageRepository.findAllUrlByReviewId(r.getId()),
                        reviewLikeRepository.existsByReviewIdAndMemberId(r.getId(), memberId)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public float getScore(long ticketId) {
        List<Review> reviewList = reviewRepository.findAllByTicketId(ticketId);
        if (reviewList.size() == 0) return 0;
        float scoreSum = 0;
        for (Review review : reviewList) scoreSum += review.getScore();
        return scoreSum / reviewList.size();
    }

    private List<ReviewCommentResponseDto> getReviewCommentResponseDtoList(long ticketId, long reviewId) {
        List<ReviewCommentResponseDto> reviewCommentResponseDtoList = new ArrayList<>();
        List<Review> reviewList = reviewRepository.getReviewList(ticketId, reviewId);
        reviewList.forEach(r -> {
            List<Comment> commentList = commentRepository.findAllByReviewId(r.getId());
            List<String> commentString = commentList.stream().map(Comment::getContent).toList();
            ReviewCommentResponseDto reviewCommentResponseDto = ReviewCommentResponseDto.of(r, commentString);
            reviewCommentResponseDtoList.add(reviewCommentResponseDto);
        });
        return reviewCommentResponseDtoList;
    }

    private void saveReviewImage(Review review, Ticket ticket, List<MultipartFile> centerImageList) {
        centerImageList.forEach(i -> {
            String reviewImageKey = saveS3Img(i);
            String reviewImageUrl = amazonS3Service.getFileUrl(reviewImageKey);
            ReviewImage reviewImage = ReviewImage.of(reviewImageKey, reviewImageUrl, review, ticket);
            reviewImageRepository.save(reviewImage);
        });
    }

    private String saveS3Img(MultipartFile profileImg) {
        try {
            return amazonS3Service.upload(profileImg, "ReviewImage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
