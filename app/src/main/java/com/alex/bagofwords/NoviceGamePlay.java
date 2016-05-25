package com.alex.bagofwords;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;

import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.AlertDialog.*;


public class NoviceGamePlay extends AppCompatActivity {

    TextView timerTextView;
    final long startTime = 13 * 1000; // 13 seconds
    final long intervals = 1000; // intervals of 10 seconds
    int completionTime;
    int count;
    GameCountDownTimer timer;

    // Instantiation of boxes
    Button fieldOne;
    Button fieldTwo;
    Button fieldThree;
    Button fieldFour;
    Button fieldFive;
    Button finishBtn;

    final String punctuationMissing = "#ff4d4d"; // colour red

    String randomSentence;    // Instantiation for randomly selected sentence stored on application
    String userRearrangement;   // Instantiation of users rearrangement of the words
    int matches;    // Instantiation of matched words between correct sentence & users rearrangement
    int score;      // Instantiation of score of game

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice_game_play);

        // Start count down timer
        timerTextView = (TextView) findViewById(R.id.timer);
        timer = new GameCountDownTimer(startTime, intervals);
        timer.start();


        randomSentence = Sentences.pickRandomNoviceSentence(); // select random sentence stored on application
        final String initialSplit[] = randomSentence.split("\\s+|(?=\\W)"); // splits selected sentence into array

        final String shuffleSentence[] = Sentences.shuffleSentence(initialSplit); // shuffle selected split sentence randomly


        fieldOne = (Button) findViewById(R.id.firstBtn);
        fieldTwo = (Button) findViewById(R.id.secondBtn);
        fieldThree = (Button) findViewById(R.id.thirdBtn);
        fieldFour = (Button) findViewById(R.id.fourthBtn);
        fieldFive = (Button) findViewById(R.id.fifthBtn);

        // populate boxes with randomly shuffled words
        fieldOne.setText(shuffleSentence[0]);
        fieldTwo.setText(shuffleSentence[1]);
        fieldThree.setText(shuffleSentence[2]);
        fieldFour.setText(shuffleSentence[3]);

        // Instantiation of drag gesture
        findViewById(R.id.firstBtn).setOnLongClickListener(dragListener);
        findViewById(R.id.secondBtn).setOnLongClickListener(dragListener);
        findViewById(R.id.thirdBtn).setOnLongClickListener(dragListener);
        findViewById(R.id.fourthBtn).setOnLongClickListener(dragListener);

        // Instantiation of drop & swap gesture
        findViewById(R.id.firstBtn).setOnDragListener(dropAndSwap);
        findViewById(R.id.secondBtn).setOnDragListener(dropAndSwap);
        findViewById(R.id.thirdBtn).setOnDragListener(dropAndSwap);
        findViewById(R.id.fourthBtn).setOnDragListener(dropAndSwap);


        // --- Round Completion ----
        finishBtn = (Button) findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(punctuationFieldEmpty()) {  // check if punctuation is not missing
                    timer.cancel(); // Stop timer
                    completionTime = timer.completionTime(); // get the round completion time
                    count = timer.timeRemaining();  // get time remaining of round
                    UserSessionHandler userSessionHandler = new UserSessionHandler(getApplicationContext());
                    userRearrangement = fieldOne.getText() + " " + fieldTwo.getText() + " " + fieldThree.getText() + " " + fieldFour.getText() + fieldFive.getText(); // concatenate users rearrangement
                    matches = Sentences.evaluate(randomSentence, userRearrangement); // Number of matches between correct and user rearrangement sentence
                    score = Sentences.gameScore(matches, count); // get score of round
                    userSessionHandler.updateScore(score); // update users score
                    showDialog(v); // display round stats

                } else {
                    fieldFive.setBackgroundColor(Color.parseColor(punctuationMissing)); // Alert user of missing punctuation mark at end of sentence
                    Toast.makeText(getApplicationContext(), "Missing punctuation, please select", Toast.LENGTH_SHORT).show();
                    Vibrator punctuationMissing = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    punctuationMissing.vibrate(200); // vibrate device
                }
            }
        });


    }

    // --- Drag box/word listener ---
    View.OnLongClickListener dragListener = new View.OnLongClickListener() { // Android gesture
        public boolean onLongClick(View v) { // onClick gesture

            DragShadow dragShadow = new DragShadow(v); //

            ClipData data = ClipData.newPlainText("",""); //
            v.startDrag(data, dragShadow, v, 0); // Move dragged object
            return false;
        }
    };

    // Visible shadow of dragged object
    private class DragShadow extends View.DragShadowBuilder {

        ColorDrawable shadow; // Instantiate shadow
        public DragShadow(View v) {
            super(v);
            shadow = new ColorDrawable(Color.LTGRAY); // Set shadow colour to gray
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);    // Display shadow on drag
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) { // Dimensions of dragged object (i.e. shadow box)
            View v = getView();
            int height = (int) v.getHeight();   // Height of dragged box
            int width = (int) v.getWidth();     // Wight of dragged box

            shadow.setBounds(0, 0, width, height);
            shadowSize.set(width, height);
            shadowTouchPoint.set((int)width/2, (int)height/2); // Shadow displayed is half size of dragged object
        }
    }

    // --- Drop and Swap words/boxes ---
    View.OnDragListener dropAndSwap = new View.OnDragListener() { // Android gestures

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED: // Dragged object/word listener
                    break;

                case DragEvent.ACTION_DRAG_EXITED: // Drag terminated
                    break;

                case DragEvent.ACTION_DROP: // Drop object/word on droppable object

                    Button dragged = (Button) event.getLocalState();    // dragged object/box
                    Button target = (Button) v;                         // target drop (i.e where dragged object is dropped)
                    String targetInitialText = (String) target.getText(); // Store word occupied in target box

                    // Swap words of the dragged and target objects
                    target.setText(dragged.getText());          // set the word of target object to the dragged objects word
                    dragged.setText(targetInitialText);         // set the word of dragged object to the word of target object
                    break;
            }
            return true;
        }
    };

    // --- Full stop onClick, populate full stop at end of sentence ---
    public void fullStopOnClick(View view) {
        Button fullStop = (Button) view;
        fieldFive.setText(fullStop.getText()); // set punctuation mark at end of sentence to full stop
        Log.d("Punctuation", "Full stop pressed!");
    }

    // --- Question mark onClick, populate question mark at end of sentence ---
    public void questionMarkOnClick(View view) {
        Button questionMark = (Button) view;
        fieldFive.setText(questionMark.getText());  // set punctuation mark at end of sentence to question mark
        Log.d("Punctuation", "Question mark pressed!");
    }

    // --- Exclamation mark onClick, populate question mark at end of sentence ---
    public void exclamationMarkOnClick(View view) {
        Button exclamationMark = (Button) view;
        fieldFive.setText(exclamationMark.getText());   // set punctuation mark at end of sentence to exclamation mark
        Log.d("Punctuation", "Exclamation mark pressed!");
    }

    // Boolean method to check if punctuation is empty or not
    public boolean punctuationFieldEmpty() {
        if(fieldFive.getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    // --- Device back button pressed ----
    @Override
    public void onBackPressed() {
        Builder alertDialog = new Builder(this);
        alertDialog.setMessage("Sure you wish to quit the game?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {    // Quit game and bring user back to main activity.
                    //NoviceGamePlay.super.onBackPressed();
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() { // Close alert dialog and allow user to continue
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.create().show();
    }


    // --- Display round stats after completion of round
    public void showDialog(View view) {
        Bundle passData = new Bundle();
        passData.putString("correctSentence", randomSentence);
        passData.putString("userSentence", userRearrangement);
        passData.putInt("matches", matches);
        passData.putInt("time", completionTime);
        passData.putInt("score", score);
        RoundStats dialog = new RoundStats();
        dialog.setArguments(passData);
        dialog.show(getFragmentManager(), "Round Stats");
    }

    // Game countdown timer abstract class
    public class GameCountDownTimer extends CountDownTimer {

        private int completionCount;
        private int countDown;

        public GameCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            countDown = (int) (millisInFuture/1000);

        }

        @Override       // Out of time
        public void onFinish() {
            TimesUp dialog = new TimesUp();
            dialog.show(getFragmentManager(), "Out of Time");
            Vibrator onTimeFinished = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE); // Instantiate vibration
            onTimeFinished.vibrate(350);    // Vibrate device if user runs out of time
        }

        @Override       //
        public void onTick(long millisUntilFinished) {
            timerTextView.setText(Long.toString(millisUntilFinished/1000)); // Change time displayed on game play activity
            completionCount++;
            countDown--;
        }

        // Completion time of round
        public int completionTime() {
            return completionCount;
        }

        // Time remaining of round
        public int timeRemaining() {
            return countDown;
        }

    }


}


