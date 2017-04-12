package com.oreilly.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by hyan on 4/12/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JokeServiceTest {
    @Autowired
    private JokeService service;

    @Test
    public void getJoke() throws Exception {
        String joke = service.getJoke("Craig", "Walls");
        assertThat(joke, containsString("Craig"));
        assertThat(joke, containsString("Walls"));
    }
}
