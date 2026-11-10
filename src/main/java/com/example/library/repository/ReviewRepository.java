package com.example.library.repository;

import com.example.library.model.PublicReview;
import com.example.library.model.Review;
import com.example.library.model.VerifiedReview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository extends BaseJdbcRepository<Review> {
    public ReviewRepository(JdbcTemplate jdbc) {
        super(jdbc, "reviews", "CREATE TABLE IF NOT EXISTS reviews (" +
                "id VARCHAR(32) PRIMARY KEY, user_id VARCHAR(32) NOT NULL, book_id VARCHAR(32) NOT NULL, " +
                "rating INT NOT NULL, comment TEXT, review_type VARCHAR(20) NOT NULL, borrow_transaction_id VARCHAR(32))");
        importLegacyFile("data/reviews.txt", this::deserializeLegacy);
    }

    @Override
    protected RowMapper<Review> rowMapper() {
        return (rs, rowNum) -> "VERIFIED".equalsIgnoreCase(rs.getString("review_type"))
                ? new VerifiedReview(rs.getString("id"), rs.getString("user_id"), rs.getString("book_id"),
                        rs.getInt("rating"), rs.getString("comment"), rs.getString("borrow_transaction_id"))
                : new PublicReview(rs.getString("id"), rs.getString("user_id"), rs.getString("book_id"),
                        rs.getInt("rating"), rs.getString("comment"));
    }

    @Override
    public Review save(Review review) {
        boolean verified = review instanceof VerifiedReview;
        String type = verified ? "VERIFIED" : "PUBLIC";
        String txId = verified ? ((VerifiedReview) review).getBorrowTransactionId() : null;
        jdbc.update("INSERT INTO reviews (id, user_id, book_id, rating, comment, review_type, borrow_transaction_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), book_id=VALUES(book_id), " +
                        "rating=VALUES(rating), comment=VALUES(comment), review_type=VALUES(review_type), " +
                        "borrow_transaction_id=VALUES(borrow_transaction_id)",
                review.getId(), review.getUserId(), review.getBookId(), review.getRating(), review.getComment(), type, txId);
        return review;
    }

    private Review deserializeLegacy(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        return "VERIFIED".equalsIgnoreCase(p[5])
                ? new VerifiedReview(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p.length > 6 ? p[6] : "")
                : new PublicReview(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4]);
    }
}
