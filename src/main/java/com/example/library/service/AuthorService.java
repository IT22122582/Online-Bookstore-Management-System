package com.example.library.service;

import com.example.library.model.Author;
import com.example.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(String id) {
        return authorRepository.findById(id);
    }

    public List<Author> searchAuthors(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllAuthors();
        }
        String lowerQuery = query.toLowerCase();
        return authorRepository.findAll().stream()
                .filter(a -> a.getName().toLowerCase().contains(lowerQuery) 
                          || a.getBio().toLowerCase().contains(lowerQuery)
                          || a.getId().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public Author saveAuthor(Author author) {
        if (author.getId() == null || author.getId().trim().isEmpty()) {
            author.setId("A" + System.currentTimeMillis() % 1000000);
        }
        return authorRepository.save(author);
    }

    public boolean deleteAuthor(String id) {
        return authorRepository.deleteById(id);
    }
}
