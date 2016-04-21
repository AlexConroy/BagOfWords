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
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;



public class GamePlay extends AppCompatActivity {


    Button fieldOne;
    Button fieldTwo;
    Button fieldThree;
    Button fieldFour;

    Button targetOne;
    Button targetTwo;
    Button targetThree;
    Button targetFour;

    Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);


        if(!isNetworkAvailable(getApplicationContext())) {
            deviceWifiSettings();
        }


        fieldOne = (Button) findViewById(R.id.firstBtn);
        fieldTwo = (Button) findViewById(R.id.secondBtn);
        fieldThree = (Button) findViewById(R.id.thirdBtn);
        fieldFour = (Button) findViewById(R.id.fourthBtn);

        fieldOne.setText("First");
        fieldTwo.setText("Second");
        fieldThree.setText("Third");
        fieldFour.setText("Fourth");


        findViewById(R.id.firstBtn).setOnLongClickListener(longListen);
        findViewById(R.id.secondBtn).setOnLongClickListener(longListen);
        findViewById(R.id.thirdBtn).setOnLongClickListener(longListen);
        findViewById(R.id.fourthBtn).setOnLongClickListener(longListen);

        findViewById(R.id.targetDropOne).setOnDragListener(DropListner);
        findViewById(R.id.targetDropTwo).setOnDragListener(DropListner);
        findViewById(R.id.targetDropThree).setOnDragListener(DropListner);
        findViewById(R.id.targetDropFour).setOnDragListener(DropListner);

        targetOne = (Button) findViewById(R.id.targetDropOne);
        targetTwo = (Button) findViewById(R.id.targetDropTwo);
        targetThree = (Button) findViewById(R.id.targetDropThree);
        targetFour = (Button) findViewById(R.id.targetDropFour);

        returnBtn = (Button) findViewById(R.id.returnBtn);


        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " First Drop field text is: " + targetOne.getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), " Second Drop field text is: " + targetTwo.getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), " Third Drop field text is: " + targetThree.getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), " Fourth Drop field text is: " + targetFour.getText().toString(), Toast.LENGTH_SHORT).show();
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
                    //log.i("");
                    //TextView target = (TextView) v;
                    //TextView dragged = (TextView) event.getLocalState();
                    Button target = (Button) v;
                    Button dragged = (Button) event.getLocalState();
                    target.setText(dragged.getText());
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
                //GamePlay.super.onBackPressed();
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
