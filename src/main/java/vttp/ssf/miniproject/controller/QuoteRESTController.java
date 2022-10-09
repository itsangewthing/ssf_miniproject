package vttp.ssf.miniproject.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.ssf.miniproject.service.UserService;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping
public class QuoteRESTController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{email}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getList(@PathVariable(name = "email") String email) {

        if (!userService.existingUser(email)) {
            JsonObject errorMsg = Json.createObjectBuilder()
                .add("error", "User does not exist")
                .build();
                
            String text = errorMsg.toString();

            return ResponseEntity
                .badRequest()
                .body(text);
        }

        List<String> savedQuotes = userService.retrieveQuotes(email);

        if (savedQuotes == null) {

            JsonObject errorMsg = Json.createObjectBuilder()
                .add("error", "User does not have saved quotes")
                .build();
                
            String text = errorMsg.toString();

            return ResponseEntity
                .badRequest()
                .body(text);
        }

        List<String> list = new LinkedList<>();
        int i = 1;

        for (String q : savedQuotes) {
            if (!q.equals("")) {
                JsonObject builder = Json.createObjectBuilder()
                .add("Quote " + i, q)
                .build();
                i++;
                String jsonObject = builder.toString();
                list.add(jsonObject);
            }
            
        }
        
        return ResponseEntity.ok(list.toString());
    }
    
}
