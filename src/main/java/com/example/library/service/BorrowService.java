package com.example.library.service;

import com.example.library.model.*;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowTransactionRepository;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowService {
    @Autowired
    private BorrowTransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BorrowTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<BorrowTransaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    public BorrowTransaction borrowBook(String userId, String bookId, int durationDays) {
        // 1. Verify user exists and check limit
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        if (user instanceof RegularUser) {
            RegularUser regUser = (RegularUser) user;
            long activeBorrows = transactionRepository.findAll().stream()
                    .filter(t -> t.getUserId().equals(userId) && !t.isReturned())
                    .count();
            if (activeBorrows >= regUser.getMaxBooksAllowed()) {
                throw new IllegalStateException("User has reached their borrowing limit of " + regUser.getMaxBooksAllowed() + " books.");
            }
        }

        // 2. Verify book exists and is available
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is already borrowed: " + book.getTitle());
        }

        // 3. Mark book unavailable
        book.setAvailable(false);
        bookRepository.save(book);

        // 4. Create and save transaction
        String txId = "T" + System.currentTimeMillis() % 1000000;
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(durationDays);
        BorrowTransaction tx = new BorrowTransaction(txId, userId, bookId, borrowDate, dueDate, false, 0.0);
        
        return transactionRepository.save(tx);
    }

    public BorrowTransaction returnBook(String transactionId) {
        BorrowTransaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
        
        if (tx.isReturned()) {
            throw new IllegalStateException("Book is already marked returned.");
        }

        // Mark book available
        Book book = bookRepository.findById(tx.getBookId()).orElse(null);
        if (book != null) {
            book.setAvailable(true);
            bookRepository.save(book);
        }

        // Calculate fine (Polymorphism)
        User user = userRepository.findById(tx.getUserId()).orElse(null);
        FineCalculator calculator = new RegularUserFineCalculator(); // default
        if (user instanceof RegularUser) {
            RegularUser regUser = (RegularUser) user;
            if ("PREMIUM".equalsIgnoreCase(regUser.getMembershipType())) {
                calculator = new PremiumUserFineCalculator();
            }
        }
        
        double fine = calculator.calculateFine(tx.getReturnDate(), LocalDate.now());
        
        tx.setReturned(true);
        tx.setReturnDate(LocalDate.now()); // set actual return date
        tx.setFinePaid(fine);
        
        return transactionRepository.save(tx);
    }
}
