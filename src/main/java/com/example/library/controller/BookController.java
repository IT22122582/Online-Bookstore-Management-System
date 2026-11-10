package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.EBook;
import com.example.library.model.PrintedBook;
import com.example.library.service.BookService;
import com.example.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public String listBooks(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("books", bookService.searchBooks(search));
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("search", search);
        return "books";
    }

    @PostMapping("/add")
    public String addBook(
            @RequestParam("title") String title,
            @RequestParam("authorId") String authorId,
            @RequestParam("isbn") String isbn,
            @RequestParam("genre") String genre,
            @RequestParam("bookType") String bookType,
            @RequestParam(value = "fileSizeMB", required = false) Double fileSizeMB,
            @RequestParam(value = "downloadUrl", required = false) String downloadUrl,
            @RequestParam(value = "shelfLocation", required = false) String shelfLocation,
            @RequestParam(value = "weightKg", required = false) Double weightKg) {
        
        Book book;
        if ("EBOOK".equalsIgnoreCase(bookType)) {
            book = new EBook(null, title, authorId, isbn, genre, true, fileSizeMB != null ? fileSizeMB : 0.0, downloadUrl);
        } else {
            book = new PrintedBook(null, title, authorId, isbn, genre, true, shelfLocation, weightKg != null ? weightKg : 0.0);
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @PostMapping("/edit")
    public String editBook(
            @RequestParam("id") String id,
            @RequestParam("title") String title,
            @RequestParam("authorId") String authorId,
            @RequestParam("isbn") String isbn,
            @RequestParam("genre") String genre,
            @RequestParam("bookType") String bookType,
            @RequestParam("available") boolean available,
            @RequestParam(value = "fileSizeMB", required = false) Double fileSizeMB,
            @RequestParam(value = "downloadUrl", required = false) String downloadUrl,
            @RequestParam(value = "shelfLocation", required = false) String shelfLocation,
            @RequestParam(value = "weightKg", required = false) Double weightKg) {
        
        Book book;
        if ("EBOOK".equalsIgnoreCase(bookType)) {
            book = new EBook(id, title, authorId, isbn, genre, available, fileSizeMB != null ? fileSizeMB : 0.0, downloadUrl);
        } else {
            book = new PrintedBook(id, title, authorId, isbn, genre, available, shelfLocation, weightKg != null ? weightKg : 0.0);
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") String id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
