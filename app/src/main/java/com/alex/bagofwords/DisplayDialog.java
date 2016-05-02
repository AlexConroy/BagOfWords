package com.alex.bagofwords;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DisplayDialog extends DialogFragment{

    TextView correctSentenceTextView;
    TextView userSentenceTextView;
    TextView scoreTextView;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.score_display_dialog, null);
        TextView correctSentenceTextView = (TextView) view.findViewById(R.id.displayCorrectSentnce);
        TextView userSentenceTextView = (TextView) view.findViewById(R.id.displayUserSentence);
        TextView scoreTextView = (TextView) view.findViewById(R.id.userScore);
        TextView completionTime = (TextView) view.findViewById(R.id.time);

        Bundle getSentence = getArguments();
        String correctSentence = getSentence.getString("correctSentence");
        String userSentence = getSentence.getString("userSentence");
        int userScore = getSentence.getInt("score");
        int userTime = getSentence.getInt("time");

        correctSentenceTextView.setText(correctSentence);
        userSentenceTextView.setText(userSentence);
        scoreTextView.setText(Integer.toString(userScore));
        completionTime.setText(Integer.toString(userTime));

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
