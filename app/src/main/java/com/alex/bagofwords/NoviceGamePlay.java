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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class NoviceGamePlay extends AppCompatActivity {

    Button fieldOne;
    Button fieldTwo;
    Button fieldThree;
    Button fieldFour;

    Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice_game_play);

        Toast.makeText(getApplicationContext(), "Number of Novice: " + Sentences.numberOfNoviceSentences(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Number of Beginner: " + Sentences.numberOfBeginnerSentences(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Number of Intermediate: " + Sentences.numberOfIntermediateSentences(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Number of Advanced: " + Sentences.numberOfAdvancedSentences(), Toast.LENGTH_SHORT).show();

        final String randomSentence = Sentences.pickRandomNoviceSentence(); // set random sentence
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



        returnBtn = (Button) findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSharedPrefHandler userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());
                String userReturnedValue = fieldOne.getText() + " " + fieldTwo.getText() + " " + fieldThree.getText() + " " + fieldFour.getText();
                Toast.makeText(getApplicationContext(), "User input: " + userReturnedValue, Toast.LENGTH_SHORT).show();
                int score = Sentences.evaluate(randomSentence, userReturnedValue);
                userSharedPrefHandler.updateScore(score);
                Toast.makeText(getApplicationContext(), "Scored: " + score, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Sure you wish to quit the game?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close alert dialog and do nothing
            }
        });
        alertDialog.create().show();
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void deviceWifiSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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

}
