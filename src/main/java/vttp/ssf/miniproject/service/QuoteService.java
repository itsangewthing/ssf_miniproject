package vttp.ssf.miniproject.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vttp.ssf.miniproject.model.Quote;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class QuoteService {
    
    private static final String url = "https://zenquotes.io/api";

    public List<Quote> getQuotesList() {

        String payload;

        // Create url with query string (add parameters)
        String uri = UriComponentsBuilder.fromUriString(url)
        .path("/quotes")
        .toUriString();

        RequestEntity<Void> req = RequestEntity.get(uri).build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            resp = template.exchange(req, String.class);
        } catch (Exception e) {
            System.err.printf("Error: %s\n", e);
            return Collections.emptyList();
        }

        if (resp.getStatusCodeValue() != 200) {
            System.err.println("Error status code is not 200\n");
            return Collections.emptyList();
        }

        // Get payload 
        payload = resp.getBody();
        // System.out.println(">>> Payload: \n" + payload);
        
        // Convert payload into JsonObject
        // Convert string to a Reader
        Reader strReader = new StringReader(payload);
        // Create a JsonReader from reader
        JsonReader jsonReader = Json.createReader(strReader);
        // Read and save the payload as Json Object
        JsonArray array = jsonReader.readArray();

        List<Quote> list = new LinkedList<>();

        for (int i = 0; i < array.size(); i++) {
            JsonObject jo = array.getJsonObject(i);
            list.add(Quote.create(jo));
        }
        return list;
    }

    public Quote getTodayQuote() {

        String payload;

        // Create url with query string (add parameters)
        String uri = UriComponentsBuilder.fromUriString(url)
        .path("/today")
        .toUriString();

        RequestEntity<Void> req = RequestEntity.get(uri).build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            resp = template.exchange(req, String.class);
        } catch (Exception e) {
            System.err.printf("Error: %s\n", e);
            return (Quote) Collections.emptyList();
        }

        if (resp.getStatusCodeValue() != 200) {
            System.err.println("Error status code is not 200\n");
            return (Quote) Collections.emptyList();
        }

        // Get payload 
        payload = resp.getBody();
        // System.out.println(">>> Payload: \n" + payload);
        
        // Convert payload into JsonObject
        // Convert string to a Reader
        Reader strReader = new StringReader(payload);
        // Create a JsonReader from reader
        JsonReader jsonReader = Json.createReader(strReader);
        // Read and save the payload as Json Object
        JsonArray array = jsonReader.readArray();

        Quote q = new Quote();

        for (int i = 0; i < array.size(); i++) {
            JsonObject jo = array.getJsonObject(i);
            q = Quote.createQuote(jo);
        }
        return q;
    }

    public Quote getRandomQuote() {

        String payload;

        // Create url with query string (add parameters)
        String uri = UriComponentsBuilder.fromUriString(url)
        .path("/random")
        .toUriString();

        RequestEntity<Void> req = RequestEntity.get(uri).build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            resp = template.exchange(req, String.class);
        } catch (Exception e) {
            System.err.printf("Error: %s\n", e);
            return (Quote) Collections.emptyList();
        }

        if (resp.getStatusCodeValue() != 200) {
            System.err.println("Error status code is not 200\n");
            return (Quote) Collections.emptyList();
        }

        // Get payload 
        payload = resp.getBody();
        // System.out.println(">>> Payload: \n" + payload);
        
        // Convert payload into JsonObject
        // Convert string to a Reader
        Reader strReader = new StringReader(payload);
        // Create a JsonReader from reader
        JsonReader jsonReader = Json.createReader(strReader);
        // Read and save the payload as Json Object
        JsonArray array = jsonReader.readArray();

        Quote q = new Quote();

        for (int i = 0; i < array.size(); i++) {
            JsonObject jo = array.getJsonObject(i);
            q = Quote.createQuote(jo);
        }
        return q;
    }
}
