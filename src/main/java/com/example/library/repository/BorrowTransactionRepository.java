package com.example.library.repository;

import com.example.library.model.BorrowTransaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class BorrowTransactionRepository extends BaseJdbcRepository<BorrowTransaction> {
    public BorrowTransactionRepository(JdbcTemplate jdbc) {
        super(jdbc, "borrow_transactions", "CREATE TABLE IF NOT EXISTS borrow_transactions (" +
                "id VARCHAR(32) PRIMARY KEY, user_id VARCHAR(32) NOT NULL, book_id VARCHAR(32) NOT NULL, " +
                "borrow_date DATE NOT NULL, return_date DATE NOT NULL, returned BOOLEAN NOT NULL, fine_paid DECIMAL(10,2) NOT NULL)");
        importLegacyFile("data/borrowed_books.txt", this::deserializeLegacy);
    }

    @Override
    protected RowMapper<BorrowTransaction> rowMapper() {
        return (rs, rowNum) -> new BorrowTransaction(rs.getString("id"), rs.getString("user_id"), rs.getString("book_id"),
                rs.getDate("borrow_date").toLocalDate(), rs.getDate("return_date").toLocalDate(),
                rs.getBoolean("returned"), rs.getDouble("fine_paid"));
    }

    @Override
    public BorrowTransaction save(BorrowTransaction tx) {
        jdbc.update("INSERT INTO borrow_transactions (id, user_id, book_id, borrow_date, return_date, returned, fine_paid) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), book_id=VALUES(book_id), " +
                        "borrow_date=VALUES(borrow_date), return_date=VALUES(return_date), returned=VALUES(returned), fine_paid=VALUES(fine_paid)",
                tx.getId(), tx.getUserId(), tx.getBookId(), tx.getBorrowDate(), tx.getReturnDate(), tx.isReturned(), tx.getFinePaid());
        return tx;
    }

    private BorrowTransaction deserializeLegacy(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 7) return null;
        return new BorrowTransaction(p[0], p[1], p[2], LocalDate.parse(p[3]), LocalDate.parse(p[4]),
                Boolean.parseBoolean(p[5]), Double.parseDouble(p[6]));
    }
}
