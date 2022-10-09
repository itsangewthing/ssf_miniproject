package vttp.ssf.miniproject.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {

    private String email;
    private String password;
    private List<String> quotes;

    public String getEmail() {      return email;       }
    public void setEmail(String email) {    this.email = email;     }

    public String getPassword() {   return password;        }
    public void setPassword(String password) {      this.password = password;   }

    public List<String> getQuotes() {   return quotes;        }
    public void setQuotes(List<String> quotes) {      this.quotes = quotes;   }

    // Create new user
    public User create(String email, String password) {
        User u = new User();
        quotes = new ArrayList<>();
        u.setEmail(email);
        u.setPassword(password);
        u.setQuotes(quotes);
        return u;
    }

    String concatQuotes = "";

    public JsonObject toJson(User u) {

        for (int i = 0; i < quotes.size(); i++) {
            concatQuotes = concatQuotes.concat("#").concat(quotes.get(i));
        }
        // System.out.println("CONCAT: " + concatQuotes);
        return Json.createObjectBuilder()
            .add("email", email)
            .add("password", password)
            .add("favourite quotes", concatQuotes)
            .build();
    }

    public static User create(JsonObject jo) {

        User u = new User();
        u.setEmail(jo.getString("email"));
        u.setPassword(jo.getString("password"));

        List<String> quotes = new ArrayList<>();
        String allQuotes = jo.getString("favourite quotes");
        // System.out.println(allQuotes);
        
        String[] separatedQuotes = allQuotes.split("#");

        for (int i = 0; i < separatedQuotes.length; i++) {
            quotes.add(separatedQuotes[i]);
        }

        u.setQuotes(quotes);
        return u;

    }

}
