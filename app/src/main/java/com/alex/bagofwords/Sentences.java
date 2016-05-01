package com.alex.bagofwords;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sentences {

    static ArrayList<String> noviceSentences = new ArrayList<String>();
    static ArrayList<String> beginnerSentences = new ArrayList<String>();
    static ArrayList<String> intermediateSentences = new ArrayList<String>();
    static ArrayList<String> advancedSentences = new ArrayList<String>();
    static Random random;

    // ----- Methods for novice sentences -------
    public static void addNoviceSentence(String sentence) {
        noviceSentences.add(sentence);
    }

    public static int numberOfNoviceSentences() {
        return noviceSentences.size();
    }

    public static String getNoviceSentence(int position) {
        return noviceSentences.get(position);
    }

    public static String pickRandomNoviceSentence() {
        random = new Random();
        String randomSentence = noviceSentences.get(random.nextInt(numberOfNoviceSentences()));
        return randomSentence;
    }

    public static boolean noviceNotEmpty() {
        return numberOfNoviceSentences() != 0;
    }

    // ---------- END -----------------


    // ------ Beginner Methods --------

    public static void addBeginnerSentence(String sentence) {
        beginnerSentences.add(sentence);
    }

    public static int numberOfBeginnerSentences() {
        return beginnerSentences.size();
    }

    public static String getBeginnerSentence(int position) {
        return beginnerSentences.get(position);
    }

    public static String pickRandomBeginnerSentence() {
        random = new Random();
        String randomSentence = beginnerSentences.get(random.nextInt(numberOfBeginnerSentences()));
        return randomSentence;
    }

    public static boolean beinngerNotEmpty() {
        return numberOfBeginnerSentences() != 0;
    }

    // --------- END --------------

    // ---------- Intermediate Methods -----------
    public static void addIntermediateSentence(String sentence) {
        intermediateSentences.add(sentence);
    }

    public static int numberOfIntermediateSentences() {
        return intermediateSentences.size();
    }

    public static String getIntermediateSentence(int position) {
        return intermediateSentences.get(position);
    }

    public static String pickRandomIntermediateSentence() {
        random = new Random();
        String randomSentence = intermediateSentences.get(random.nextInt(numberOfIntermediateSentences()));
        return randomSentence;
    }

    public static boolean intermediateNotEmpty() {
        return numberOfIntermediateSentences() != 0;
    }

    // ----------------------

    // ---- Advanced Methods -------

    public static void addAdvancedSentence(String sentence) {
        advancedSentences.add(sentence);
    }

    public static int numberOfAdvancedSentences() {
        return advancedSentences.size();
    }

    public static String getAdvancedSentence(int position) {
        return advancedSentences.get(position);
    }

    public static String pickRandomIAdvancedSentence() {
        random = new Random();
        String randomSentence = advancedSentences.get(random.nextInt(numberOfAdvancedSentences()));
        return randomSentence;
    }

    public static boolean advancedNotEmpty() {
        return numberOfAdvancedSentences() != 0;
    }

    // ------ END ---------



    public static int evaluate(String[] correctSentence, String[] userInputSentence) {
        int size = correctSentence.length;
        int score = 0;
        for(int i = 0; i < size; i++) {
            if(correctSentence[i].equals(userInputSentence)) {
                score++;
            }
        }
        return score;
    }

    public static int evaluate(String correctSentence, String userInputSentence) {
        String splitCorrectSentence[] = correctSentence.split("\\s+");
        String splitInputedSentence[] = userInputSentence.split("\\s+");
        int size = splitCorrectSentence.length;
        int score = 0;
        for(int i = 0; i < size; i++) {
            if(splitCorrectSentence[i].equals(splitInputedSentence[i])) {
                score++;
            }
        }
        return score;
    }


    public static int evaluate(String[] correctSentence, String userInputSentence) {
        String splitSentence[] = userInputSentence.split("\\s+");
        int size = correctSentence.length;
        int score = 0;
        for(int i = 0; i < size; i++) {
            if(correctSentence[i].equals(splitSentence[i])) {
                score++;
            }
        }
        return score;
    }

    public static String shuffleStringSentence(String[] sentence) {
        Random random = new Random();
        for(int i = 0; i < sentence.length; i++){
            int randomPosition = random.nextInt(sentence.length);
            String temp = sentence[i];
            sentence[i] = sentence[randomPosition];
            sentence[randomPosition] = temp;
        }
        String joinWords = TextUtils.join(" ", sentence);
        return joinWords;
    }

    public static String[] shuffleArraySentence(String[] sentence) {
        Random random = new Random();
        for(int i = 0; i < sentence.length; i++){
            int randomPosition = random.nextInt(sentence.length);
            String temp = sentence[i];
            sentence[i] = sentence[randomPosition];
            sentence[randomPosition] = temp;
        }
        return sentence;
    }





}
