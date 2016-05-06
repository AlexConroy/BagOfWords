package com.alex.bagofwords;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RoundStats extends DialogFragment{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.round_stats_layout, null);
        TextView correctSentenceTextView = (TextView) view.findViewById(R.id.displayCorrectSentnce);
        TextView userSentenceTextView = (TextView) view.findViewById(R.id.displayUserSentence);
        TextView matchedTextView = (TextView) view.findViewById(R.id.userMatches);
        TextView completionTimeTextView = (TextView) view.findViewById(R.id.time);
        TextView scoreTextView = (TextView) view.findViewById(R.id.score);

        Bundle getSentence = getArguments();
        String correctSentence = getSentence.getString("correctSentence");
        String userSentence = getSentence.getString("userSentence");
        int matches = getSentence.getInt("matches");
        int userTime = getSentence.getInt("time");
        int score = getSentence.getInt("score");

        correctSentenceTextView.setText(correctSentence);
        userSentenceTextView.setText(userSentence);
        matchedTextView.setText(Integer.toString(matches));
        completionTimeTextView.setText(Integer.toString(userTime));
        scoreTextView.setText(Integer.toString(score));

        builder.setView(view);
        setCancelable(false);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent mainMenu = new Intent(getActivity(), MainMenu.class);
                startActivity(mainMenu);
                getActivity().finish();

            }
        });

        Dialog dialog = builder.create();

        return dialog;
    }
}
