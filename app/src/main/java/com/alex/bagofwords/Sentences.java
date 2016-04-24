package com.alex.bagofwords;

import java.util.ArrayList;

public class Sentences {

    static ArrayList<String> noviceSentence = new ArrayList<String>();


    public static void addSentence(String sentence) {
        noviceSentence.add(sentence);
    }

    public static int numberOfSentences() {
        return noviceSentence.size();
    }

    public static String getSentence(int position) {
        return noviceSentence.get(position);
    }

}
