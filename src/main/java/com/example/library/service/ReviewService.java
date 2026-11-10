package com.example.library.service;

import com.example.library.model.*;
import com.example.library.repository.ReviewRepository;
import com.example.library.repository.BorrowTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BorrowTransactionRepository transactionRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByBookId(String bookId) {
        return reviewRepository.findAll().stream()
                .filter(r -> r.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    public Review submitReview(String userId, String bookId, int rating, String comment) {
        // Check if there's any transaction for this user and book (implies verification)
        Optional<BorrowTransaction> txOpt = transactionRepository.findAll().stream()
                .filter(t -> t.getUserId().equals(userId) && t.getBookId().equals(bookId))
                .findFirst();

        String reviewId = "R" + System.currentTimeMillis() % 1000000;
        Review review;

        if (txOpt.isPresent()) {
            // Verified review
            review = new VerifiedReview(reviewId, userId, bookId, rating, comment, txOpt.get().getId());
        } else {
            // Public review
            review = new PublicReview(reviewId, userId, bookId, rating, comment);
        }

        return reviewRepository.save(review);
    }

    public boolean deleteReview(String id) {
        return reviewRepository.deleteById(id);
    }
}
