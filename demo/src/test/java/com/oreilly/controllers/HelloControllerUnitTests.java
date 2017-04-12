package com.oreilly.controllers;

import org.junit.Test;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by hyan on 4/12/17.
 */
public class HelloControllerUnitTests {
    @Test
    public void testSayHello() {
        HelloController controller = new HelloController();
        String name = controller.sayHello("Tristan", new BindingAwareModelMap());
        assertEquals("hello", name);
    }
}
