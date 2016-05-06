package com.alex.bagofwords;

import android.app.AlertDialog;
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

public class BeginnerGamePLay extends AppCompatActivity {

    TextView timerTextView;
    final long startTime = 15 * 1000;
    final long intervals = 1000;
    int completionTime;
    int count;
    GameCountDownTimer timer;

    Button fieldOne;
    Button fieldTwo;
    Button fieldThree;
    Button fieldFour;
    Button fieldFive;
    Button fieldSix;
    Button finishBtn;

    final String puncutuationMissing = "#ff4d4d";

    String randomSentence;
    String userReturnedValue;
    int matches;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner_game_play);

        timerTextView = (TextView) findViewById(R.id.timer);
        timer = new GameCountDownTimer(startTime, intervals);
        timer.start();

        randomSentence = Sentences.pickRandomBeginnerSentence();
        final String initialSplit[] = randomSentence.split("\\s+|(?=\\W)");
        final String shuffleSentence[] = Sentences.shuffleArraySentence(initialSplit);

        fieldOne = (Button) findViewById(R.id.firstBtn);
        fieldTwo = (Button) findViewById(R.id.secondBtn);
        fieldThree = (Button) findViewById(R.id.thirdBtn);
        fieldFour = (Button) findViewById(R.id.fourthBtn);
        fieldFive = (Button) findViewById(R.id.fifthBtn);
        fieldSix = (Button) findViewById(R.id.sixthBtn);

        fieldOne.setText(shuffleSentence[0]);
        fieldTwo.setText(shuffleSentence[1]);
        fieldThree.setText(shuffleSentence[2]);
        fieldFour.setText(shuffleSentence[3]);
        fieldFive.setText(shuffleSentence[4]);

        findViewById(R.id.firstBtn).setOnLongClickListener(longListen);
        findViewById(R.id.secondBtn).setOnLongClickListener(longListen);
        findViewById(R.id.thirdBtn).setOnLongClickListener(longListen);
        findViewById(R.id.fourthBtn).setOnLongClickListener(longListen);
        findViewById(R.id.fifthBtn).setOnLongClickListener(longListen);

        findViewById(R.id.firstBtn).setOnDragListener(DropListner);
        findViewById(R.id.secondBtn).setOnDragListener(DropListner);
        findViewById(R.id.thirdBtn).setOnDragListener(DropListner);
        findViewById(R.id.fourthBtn).setOnDragListener(DropListner);
        findViewById(R.id.fifthBtn).setOnDragListener(DropListner);


        finishBtn = (Button) findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(punctuationFieldEmpty()) {
                    timer.cancel();
                    completionTime = timer.completionTime();
                    count = timer.timeRemaining();
                    UserSharedPrefHandler userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());
                    userReturnedValue = fieldOne.getText() + " " + fieldTwo.getText() + " " + fieldThree.getText() + " " + fieldFour.getText() + " " + fieldFive.getText() + fieldSix.getText();
                    Toast.makeText(getApplicationContext(), "User sentence: " + userReturnedValue, Toast.LENGTH_LONG).show();
                    matches = Sentences.evaluate(randomSentence, userReturnedValue);
                    score = Sentences.gameScore(matches, count);
                    userSharedPrefHandler.updateScore(score);
                    showDialog(v);

                } else {
                    fieldSix.setBackgroundColor(Color.parseColor(puncutuationMissing));
                    Toast.makeText(getApplicationContext(), "Missing punctuation, please select", Toast.LENGTH_SHORT).show();
                    Vibrator punctuationMissing = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    punctuationMissing.vibrate(200);
                }
            }
        });


    }

    View.OnLongClickListener longListen = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {

            DragShadow dragShadow = new DragShadow(v);

            ClipData data = ClipData.newPlainText("","");
            v.startDrag(data, dragShadow, v, 0);
            return false;
        }

    };

    private class DragShadow extends View.DragShadowBuilder {

        ColorDrawable greyBox;

        public DragShadow(View v) {
            super(v);
            greyBox = new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            greyBox.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            View v = getView();
            int height = (int) v.getHeight();
            int width = (int) v.getWidth();

            greyBox.setBounds(0, 0, width, height);
            shadowSize.set(width, height);
            shadowTouchPoint.set((int)width/2, (int)height/2);
        }
    }

    View.OnDragListener DropListner = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Drag Event", "Entered");
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Drag Event", "Exited");
                    break;

                case DragEvent.ACTION_DROP:

                    Button target = (Button) v;
                    Button dragged = (Button) event.getLocalState();
                    String targetInitialText = (String) target.getText();
                    target.setText(dragged.getText());
                    dragged.setText(targetInitialText);
                    break;
            }
            return true;
        }
    };

    public void fullStopOnClick(View view) {
        Button fullStop = (Button) view;
        fieldSix.setText(fullStop.getText());
        Log.d("Punctuation", "Full stop pressed!");
    }

    public void questionMarkOnClick(View view) {
        Button questionMark = (Button) view;
        fieldSix.setText(questionMark.getText());
        Log.d("Punctuation", "Question mark pressed!");
    }

    public void exclamationMarkOnClick(View view) {
        Button exclamationMark = (Button) view;
        fieldSix.setText(exclamationMark.getText());
        Log.d("Punctuation", "Exclamation mark pressed!");
    }

    public boolean punctuationFieldEmpty() {
        if(fieldSix.getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Sure you wish to quit the game?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //NoviceGamePlay.super.onBackPressed();
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }

    public void showDialog(View view) {
        Bundle passData = new Bundle();
        passData.putString("correctSentence", randomSentence);
        passData.putString("userSentence", userReturnedValue);
        passData.putInt("matches", matches);
        passData.putInt("time", completionTime);
        passData.putInt("score", score);
        RoundStats dialog = new RoundStats();
        dialog.setArguments(passData);
        dialog.show(getFragmentManager(), "My Dialog");
    }


    public class GameCountDownTimer extends CountDownTimer {

        private int completionCount;
        private int countDown;

        public GameCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            countDown = (int) (millisInFuture/1000);

        }


        @Override
        public void onFinish() {
            TimesUp dialog = new TimesUp();
            dialog.show(getFragmentManager(), "Out of Time");
            Vibrator onTimeFinished = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            onTimeFinished.vibrate(350);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerTextView.setText(Long.toString(millisUntilFinished/1000));
            completionCount++;
            countDown--;
            Log.d("Timer", Integer.toString(completionCount));
        }

        public int completionTime() {
            return completionCount;
        }

        public int timeRemaining() {
            return countDown;
        }

    }


}
