package vttp.ssf.miniproject.repositories;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import vttp.ssf.miniproject.model.User;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Repository
public class UserRepository {
    
    @Autowired
    @Qualifier("redis")
    private RedisTemplate<String, String> redisTemplate;

    
    public Optional<String> checkUser(String email) {

        if (redisTemplate.hasKey(email)) {

            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

            String result = valueOps.get(email);
            return Optional.of(result);
        }

        return Optional.empty();
    }

    public void saveUser(User u) {

        String email = u.getEmail();

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set(email, u.toJson(u).toString());

    }

    public boolean userExists(String email) {

        if (redisTemplate.hasKey(email)) {
            return true;
        }
        return false;

    }

    public void saveQuotes(String email, List<String> s) {

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String userDetails = valueOps.get(email);

        Reader stringReader = new StringReader(userDetails);
        JsonReader jsonReader = Json.createReader(stringReader);
        // Read and save the payload as Json Object
        JsonObject jo = jsonReader.readObject();
        User u = User.create(jo);

        List<String> existingQuotes;

        if (u.getQuotes() == null) {

            u.setQuotes(s);

        } else {

            existingQuotes = u.getQuotes();

            for (String singleQuote : s) {
                if (!existingQuotes.contains(singleQuote)) {
                    existingQuotes.add(singleQuote);
                }
            }
            u.setQuotes(existingQuotes);
        }

        valueOps.set(email, u.toJson(u).toString());

    }

    public List<String> retrieveQuotes(String email) {

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String userDetails = valueOps.get(email);

        Reader stringReader = new StringReader(userDetails);
        JsonReader jsonReader = Json.createReader(stringReader);
        // Read and save the payload as Json Object
        JsonObject jo = jsonReader.readObject();
        User u = User.create(jo);

        return u.getQuotes();

    }
}
