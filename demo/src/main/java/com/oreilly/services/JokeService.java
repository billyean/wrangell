package com.oreilly.services;

import com.oreilly.json.Joke;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by hyan on 4/12/17.
 */
@Service
public class JokeService {
    private RestTemplate restTemplate;

    private static final String BASE = "http://api.icndb.com/jokes/random?limitTo=[nerdy]";

    public JokeService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public String getJoke(String first, String last) {
        String url = String.format("%s&firstName=%s&lastName=%s", BASE, first, last);
        Joke joke = restTemplate.getForObject(url, Joke.class);
        return joke.getValue().getJoke();
    }
}
