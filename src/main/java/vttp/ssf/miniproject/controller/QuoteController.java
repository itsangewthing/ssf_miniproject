package com.example.miniproject.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.miniproject.model.Quote;
import com.example.miniproject.model.User;
import com.example.miniproject.service.QuoteService;
import com.example.miniproject.service.UserService;

@Controller
@RequestMapping
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private UserService userService;

    @PostMapping("/registered")
    public String registerUser(@RequestBody MultiValueMap<String, String> form, Model model) {

        //
        String email = form.getFirst("email").toLowerCase();
        String password = form.getFirst("password");

        if (!userService.existingUser(email)) {
            System.out.println("CREATING NEW USER");
            User u = new User();
            u = u.create(email, password);
            userService.saveUser(u);

            model.addAttribute("email", email);
            return "search";
        }

        return "login";
    }

    @PostMapping(path = "/loggedIn")
    public String userLogin(@RequestBody MultiValueMap<String, String> form, Model model) {

        String email = form.getFirst("email").toLowerCase();
        String password = form.getFirst("password");
        User u;
        // check if user exists
        if (!userService.existingUser(email)) {
            return "index";
        }

        u = userService.checkUser(email);

        // check password
        if (password.equals(u.getPassword())) {

            model.addAttribute("email", email);
            return "search";

        } else {

            return "index";
        }
    }

    @GetMapping("/list")
    public String getListOfQuotes(@RequestParam("email") String userEmail, Model model) {

        List<Quote> quotes = quoteService.getQuotesList();

        model.addAttribute("email", userEmail);
        model.addAttribute("quotes", quotes);

        return "results";
    }

    @GetMapping("/today")
    public String getQuoteOfTheDay(@RequestParam("email") String userEmail, Model model) {

        Quote q = quoteService.getTodayQuote();

        model.addAttribute("email", userEmail);
        model.addAttribute("quotes", q);

        return "results";
    }

    @GetMapping("/random")
    public String getRandomQuote(@RequestParam("email") String userEmail, Model model) {

        Quote q = quoteService.getTodayQuote();

        model.addAttribute("email", userEmail);
        model.addAttribute("quotes", q);

        return "results";
    }

    @PostMapping(path = "/save")
    public String saveQuotes(@RequestBody MultiValueMap<String, String> form,
            @RequestParam("saveCheckbox") List<String> checkboxValue, @RequestParam("email") String userEmail,
            Model model) {

        System.out.println("Checkbox values: " + checkboxValue);

        if (checkboxValue.size() == 1) {

            userService.saveQuotes(userEmail, checkboxValue);
        }

        List<String> savedList = new LinkedList<>();

        for (String selectedQuote : checkboxValue) {

            savedList.add(selectedQuote);
        }
        System.out.printf("SAVED LIST: %s\n\n", savedList);

        userService.saveQuotes(userEmail, savedList);

        model.addAttribute("email", userEmail);
        return "search";
    }

    @RequestMapping(path = "/{email}")
    public String retrieveQuotes(@PathVariable(name = "email") String email, Model model) {

        if (userService.existingUser(email)) {
            List<String> saved = userService.retrieveQuotes(email);
            model.addAttribute("quotes", saved);
            return "retrieved";
        } else {
            return "error";
        }

    }
}
