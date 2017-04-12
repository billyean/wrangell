package com.oreilly.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.is;

/**
 * Created by hyan on 4/12/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(HelloController.class)
public class HelloControllerIntegrationTests {
    @Autowired
    private MockMvc mock;

    @Test
    public void testHelloWithoutName() throws Exception {
        mock.perform(get("/sayHello").accept(TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attribute("name", is("World")));
    }

    @Test
    public void testHelloWithName() throws Exception {
        mock.perform(get("/sayHello").param("name", "Tristan").accept(TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attribute("name", is("Tristan")));
    }
}
