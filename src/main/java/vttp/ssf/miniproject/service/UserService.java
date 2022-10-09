package com.example.miniproject.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.miniproject.model.User;
import com.example.miniproject.repositories.UserRepository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User checkUser(String email) {

        Optional<String> opt = userRepository.checkUser(email);

        if (opt.isEmpty()) {
            return null;
        }

        String value = opt.get();

        Reader stringReader = new StringReader(value);

        // Create a JsonReader from reader
        JsonReader jsonReader = Json.createReader(stringReader);

        // Read and save the payload as Json Object
        JsonObject jObject = jsonReader.readObject();

        return User.create(jObject);
    }

    public void saveUser(User u) {

        userRepository.saveUser(u);
    }

    public boolean existingUser(String email) {

        return userRepository.userExists(email);
    }

    public void saveQuotes(String email, List<String> s) {

        userRepository.saveQuotes(email, s);

    }

    public List<String> retrieveQuotes(String email) {

        return userRepository.retrieveQuotes(email);

    }
}
