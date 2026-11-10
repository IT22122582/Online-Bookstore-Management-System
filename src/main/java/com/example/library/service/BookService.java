package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        String lowerQuery = query.toLowerCase();
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerQuery) 
                          || b.getGenre().toLowerCase().contains(lowerQuery) 
                          || b.getIsbn().toLowerCase().contains(lowerQuery)
                          || b.getId().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public Book saveBook(Book book) {
        if (book.getId() == null || book.getId().trim().isEmpty()) {
            book.setId("B" + System.currentTimeMillis() % 1000000);
        }
        return bookRepository.save(book);
    }

    public boolean deleteBook(String id) {
        return bookRepository.deleteById(id);
    }
}
