package com.example.library.controller;

import com.example.library.service.ReviewService;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("users", userService.getAllUsers());
        return "reviews";
    }

    @PostMapping("/add")
    public String addReview(
            @RequestParam("userId") String userId,
            @RequestParam("bookId") String bookId,
            @RequestParam("rating") int rating,
            @RequestParam("comment") String comment) {
        reviewService.submitReview(userId, bookId, rating, comment);
        return "redirect:/reviews";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") String id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }
}
