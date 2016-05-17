package com.alex.bagofwords;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SentencesTest {

    @Before
    public void setUp() {
        Sentences.addNoviceSentence("The mat is black.");
        Sentences.addBeginnerSentence("What time is lunch at?");
        Sentences.addIntermediateSentence("I lived through the year 2012.");
        Sentences.addAdvancedSentence("Life is like a box of chocolates.");
    }

    @Test
    public void testShuffleSentence() {
        String initialSentence = "The mat is black.";
        String result[] = initialSentence.split("\\s+|(?=\\W)");
        String correctSentenceInArray[] =  {"The", "mat", "is", "black", "."};
        assertArrayEquals(correctSentenceInArray, result);
    }

    @Test
    public void validEvaluation() {
        assertEquals(2, Sentences.evaluate("where are you from?", "where from are you?"));
        assertEquals(0, Sentences.evaluate("What time is it", "time What it is"));
        assertEquals(4, Sentences.evaluate("first second third fourth", "first second third fourth"));
        assertEquals(3, Sentences.evaluate("copy and pasted", "copy and pasted"));
    }


    @After
    public void tearDown() {

    }

}
