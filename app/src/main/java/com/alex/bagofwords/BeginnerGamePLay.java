package com.alex.bagofwords;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BeginnerGamePLay extends AppCompatActivity {

    Button fieldOne;
    Button fieldTwo;
    Button fieldThree;
    Button fieldFour;
    Button fieldFive;

    Button finishBtn;

    String randomSentence;
    String userReturnedValue;
    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner_game_play);

        fieldOne = (Button) findViewById(R.id.firstBtn);
        fieldTwo = (Button) findViewById(R.id.secondBtn);
        fieldThree = (Button) findViewById(R.id.thirdBtn);
        fieldFour = (Button) findViewById(R.id.fourthBtn);
        fieldFive = (Button) findViewById(R.id.fifthBtn);

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

        randomSentence = Sentences.pickRandomBeginnerSentence(); // set random sentence
        //Toast.makeText(getApplicationContext(), "Sentence: " + randomSentence, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Sentence picked: " + randomSentence, Toast.LENGTH_SHORT).show(); //Displays selected sentence
        final String initialSplit[] = randomSentence.split("\\s+"); // splits selected sentence into array

        final String shuffleSentence[] = Sentences.shuffleArraySentence(initialSplit); // shuffles selected sentence

        // populate shuffle words
        fieldOne.setText(shuffleSentence[0]);
        fieldTwo.setText(shuffleSentence[1]);
        fieldThree.setText(shuffleSentence[2]);
        fieldFour.setText(shuffleSentence[3]);
        fieldFive.setText(shuffleSentence[4]);

        finishBtn = (Button) findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSharedPrefHandler userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());
                userReturnedValue = fieldOne.getText() + " " + fieldTwo.getText() + " " + fieldThree.getText() + " " + fieldFour.getText() + " " + fieldFive.getText().toString();
                //Toast.makeText(getApplicationContext(), "User input: " + userReturnedValue, Toast.LENGTH_SHORT).show();
                score = Sentences.evaluate(randomSentence, userReturnedValue);
                userSharedPrefHandler.updateScore(score);
                showDialog(v);
                //Toast.makeText(getApplicationContext(), "Scored: " + score, Toast.LENGTH_LONG).show();
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

    public void showDialog(View view) {
        Bundle passData = new Bundle();
        passData.putString("correctSentence", randomSentence);
        passData.putString("userSentence", userReturnedValue);
        passData.putInt("score", score);
        RoundStats dialog = new RoundStats();
        dialog.setArguments(passData);
        dialog.show(getFragmentManager(), "My Dialog");
    }


}
