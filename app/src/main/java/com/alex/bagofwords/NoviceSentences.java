package com.alex.bagofwords;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoviceSentences {

    static ArrayList<String> noviceSentence = new ArrayList<String>();
    static Random randomSentence;

    public static void addSentence(String sentence) {
        noviceSentence.add(sentence);
    }

    public static int numberOfSentences() {
        return noviceSentence.size();
    }

    public static String getSentence(int position) {
        return noviceSentence.get(position);
    }

    public static String pickRandom() {
        randomSentence = new Random();
        String ran = noviceSentence.get(randomSentence.nextInt(numberOfSentences()));
        return ran;
    }

    public static boolean notEmpty() {
        return numberOfSentences() != 0;
    }



}
