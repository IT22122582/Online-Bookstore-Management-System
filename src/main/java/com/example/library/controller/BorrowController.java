package com.example.library.controller;

import com.example.library.model.BorrowTransaction;
import com.example.library.service.BorrowService;
import com.example.library.service.BookService;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listTransactions(Model model) {
        model.addAttribute("transactions", borrowService.getAllTransactions());
        model.addAttribute("books", bookService.getAllBooks().stream().filter(b -> b.isAvailable()).collect(Collectors.toList()));
        model.addAttribute("users", userService.getAllUsers());
        return "borrow";
    }

    @PostMapping("/checkout")
    public String checkout(
            @RequestParam("userId") String userId,
            @RequestParam("bookId") String bookId,
            @RequestParam("duration") int duration,
            Model model) {
        try {
            borrowService.borrowBook(userId, bookId, duration);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("transactions", borrowService.getAllTransactions());
            model.addAttribute("books", bookService.getAllBooks().stream().filter(b -> b.isAvailable()).collect(Collectors.toList()));
            model.addAttribute("users", userService.getAllUsers());
            return "borrow";
        }
        return "redirect:/borrow";
    }

    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable("id") String transactionId, Model model) {
        try {
            borrowService.returnBook(transactionId);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("transactions", borrowService.getAllTransactions());
            model.addAttribute("books", bookService.getAllBooks().stream().filter(b -> b.isAvailable()).collect(Collectors.toList()));
            model.addAttribute("users", userService.getAllUsers());
            return "borrow";
        }
        return "redirect:/borrow";
    }
}
