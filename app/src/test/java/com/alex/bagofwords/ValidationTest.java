package com.alex.bagofwords;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



public class ValidationTest {

    private Validation validationMethods;

    @Before
    public void setup() throws Exception {
        validationMethods = new Validation();
    }

    @Test
    public void validPasswordTest() {
        assertEquals(false, validationMethods.validPassword(""));
        assertEquals(false, validationMethods.validPassword("12345"));
        assertEquals(true, validationMethods.validPassword("password"));
        //assertEquals(true, "pasewewews");
        //assertEquals(true, "passdsd");

    }

    @After
    public void teardown() throws Exception {
        validationMethods = null;
    }



}