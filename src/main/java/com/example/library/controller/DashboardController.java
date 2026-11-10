package com.example.library.controller;

import com.example.library.service.BookService;
import com.example.library.service.UserService;
import com.example.library.service.AuthorService;
import com.example.library.service.BorrowService;
import com.example.library.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String showDashboard(Model model) {
        long totalBooks = bookService.getAllBooks().size();
        long totalUsers = userService.getAllUsers().size();
        long totalAuthors = authorService.getAllAuthors().size();
        long activeLoans = borrowService.getAllTransactions().stream()
                .filter(t -> !t.isReturned())
                .count();

        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalAuthors", totalAuthors);
        model.addAttribute("activeLoans", activeLoans);
        
        model.addAttribute("recentReviews", reviewService.getAllReviews());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("authors", authorService.getAllAuthors());
        
        return "index";
    }
}
