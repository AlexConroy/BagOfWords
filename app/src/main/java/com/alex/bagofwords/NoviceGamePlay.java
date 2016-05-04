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
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.AlertDialog.*;


public class NoviceGamePlay extends AppCompatActivity {

    TextView timer;
    final long startTime = 10 * 1000;
    final long intervals = 1000;
    int completionTime;
    int count;
    GameCountDownTimer countDownTimer;

    Button fieldOne;
    Button fieldTwo;
    Button fieldThree;
    Button fieldFour;

    Button finishBtn;

    String randomSentence;
    String userReturnedValue;
    int matches;
    int score;
    int resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice_game_play);

        timer = (TextView) findViewById(R.id.timer);
        countDownTimer = new GameCountDownTimer(startTime, intervals);
        countDownTimer.start();


        randomSentence = Sentences.pickRandomNoviceSentence(); // set random sentence
        Toast.makeText(getApplicationContext(), "Sentence picked: " + randomSentence, Toast.LENGTH_SHORT).show(); //Displays selected sentence
        final String initialSplit[] = randomSentence.split("\\s+"); // splits selected sentence into array

        final String shuffleSentence[] = Sentences.shuffleArraySentence(initialSplit); // shuffles selected sentence

        if(!isNetworkAvailable(getApplicationContext())) {
            deviceWifiSettings();
        }

        fieldOne = (Button) findViewById(R.id.firstBtn);
        fieldTwo = (Button) findViewById(R.id.secondBtn);
        fieldThree = (Button) findViewById(R.id.thirdBtn);
        fieldFour = (Button) findViewById(R.id.fourthBtn);

        // populate shuffle words
        fieldOne.setText(shuffleSentence[0]);
        fieldTwo.setText(shuffleSentence[1]);
        fieldThree.setText(shuffleSentence[2]);
        fieldFour.setText(shuffleSentence[3]);

        findViewById(R.id.firstBtn).setOnLongClickListener(longListen);
        findViewById(R.id.secondBtn).setOnLongClickListener(longListen);
        findViewById(R.id.thirdBtn).setOnLongClickListener(longListen);
        findViewById(R.id.fourthBtn).setOnLongClickListener(longListen);

        findViewById(R.id.firstBtn).setOnDragListener(DropListner);
        findViewById(R.id.secondBtn).setOnDragListener(DropListner);
        findViewById(R.id.thirdBtn).setOnDragListener(DropListner);
        findViewById(R.id.fourthBtn).setOnDragListener(DropListner);

        finishBtn = (Button) findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                completionTime = countDownTimer.completionTime();
                count = countDownTimer.timeRemaining();
                UserSharedPrefHandler userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());
                userReturnedValue = fieldOne.getText() + " " + fieldTwo.getText() + " " + fieldThree.getText() + " " + fieldFour.getText();
                matches = Sentences.evaluate(randomSentence, userReturnedValue);
                score = Sentences.gameScore(matches, count);
                countDownTimer.cancel();
                userSharedPrefHandler.updateScore(score);
                showDialog(v);

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

    boolean backButtonPressed = false;

    @Override
    public void onBackPressed() {
        Builder alertDialog = new Builder(this);
        if (!backButtonPressed) {
           // countDownTimer.cancel();
           // final int resume = countDownTimer.timeRemaining();
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
                    //GameCountDownTimer resumeTimer = new GameCountDownTimer(resume * 1000, 1000);
                   // countDownTimer = new GameCountDownTimer(1000, intervals);
                   // countDownTimer.start();
                }
            });
            alertDialog.create().show();
        }
    }



    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void deviceWifiSettings() {
        Builder alertDialog = new Builder(this);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Check network settings");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
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
        DisplayDialog dialog = new DisplayDialog();
        dialog.setArguments(passData);
        dialog.show(getFragmentManager(), "My Dialog");
    }

    public class GameCountDownTimer extends CountDownTimer {

        private int completionCount;
        private int countDown;

        public GameCountDownTimer(long millisInFuture, long countDownInterval) {
            //super(millisInFuture, countDownInterval);
            super(10000, 1000);
            countDown = (int) (millisInFuture/1000);
        }


        @Override
        public void onFinish() {
            timer.setText("Time's Up!!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText(Long.toString(millisUntilFinished/1000));
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


