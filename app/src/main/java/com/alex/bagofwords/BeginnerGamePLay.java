package com.alex.bagofwords;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
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


    Button targetOne;
    Button targetTwo;
    Button targetThree;
    Button targetFour;
    Button targetFive;

    Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner_game_play);

        fieldOne = (Button) findViewById(R.id.firstBtn);
        fieldTwo = (Button) findViewById(R.id.secondBtn);
        fieldThree = (Button) findViewById(R.id.thirdBtn);
        fieldFour = (Button) findViewById(R.id.fourthBtn);
        fieldFive = (Button) findViewById(R.id.fifthBtn);

        fieldOne.setText("First");
        fieldTwo.setText("Second");
        fieldThree.setText("Third");
        fieldFour.setText("Fourth");
        fieldFour.setText("Fifth");


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
                Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_LONG).show();
                //NoviceGamePlay.super.onBackPressed();
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
