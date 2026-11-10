package com.example.library.controller;

import com.example.library.model.Author;
import com.example.library.model.GuestAuthor;
import com.example.library.model.PermanentAuthor;
import com.example.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public String listAuthors(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("authors", authorService.searchAuthors(search));
        model.addAttribute("search", search);
        return "authors";
    }

    @PostMapping("/add")
    public String addAuthor(
            @RequestParam("name") String name,
            @RequestParam("bio") String bio,
            @RequestParam("authorType") String authorType,
            @RequestParam(value = "institution", required = false) String institution,
            @RequestParam(value = "visitDurationDays", required = false) Integer visitDurationDays,
            @RequestParam(value = "contractPeriodYears", required = false) Integer contractPeriodYears,
            @RequestParam(value = "officeNumber", required = false) String officeNumber) {
        
        Author author;
        if ("GUEST".equalsIgnoreCase(authorType)) {
            author = new GuestAuthor(null, name, bio, institution, visitDurationDays != null ? visitDurationDays : 0);
        } else {
            author = new PermanentAuthor(null, name, bio, contractPeriodYears != null ? contractPeriodYears : 0, officeNumber);
        }
        authorService.saveAuthor(author);
        return "redirect:/authors";
    }

    @PostMapping("/edit")
    public String editAuthor(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("bio") String bio,
            @RequestParam("authorType") String authorType,
            @RequestParam(value = "institution", required = false) String institution,
            @RequestParam(value = "visitDurationDays", required = false) Integer visitDurationDays,
            @RequestParam(value = "contractPeriodYears", required = false) Integer contractPeriodYears,
            @RequestParam(value = "officeNumber", required = false) String officeNumber) {
        
        Author author;
        if ("GUEST".equalsIgnoreCase(authorType)) {
            author = new GuestAuthor(id, name, bio, institution, visitDurationDays != null ? visitDurationDays : 0);
        } else {
            author = new PermanentAuthor(id, name, bio, contractPeriodYears != null ? contractPeriodYears : 0, officeNumber);
        }
        authorService.saveAuthor(author);
        return "redirect:/authors";
    }

    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable("id") String id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}
