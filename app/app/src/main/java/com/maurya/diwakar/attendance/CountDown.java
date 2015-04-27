package com.maurya.diwakar.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CountDown extends Activity {

    ArrayList<String> roll_list;
    String mUsername;
    String subject_name;
    String class_name;
    int value;

    private GestureDetectorCompat mDetector;
    TextView rollNoTextView;
    int students;
    boolean[] attRecord;
    int currentRollNo = 0;
    Vibrator vibrator;
    private Toast toast;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        roll_list = getIntent().getStringArrayListExtra("roll_list");
        mUsername = getIntent().getStringExtra("username");
        subject_name = getIntent().getStringExtra("subject");
        class_name = getIntent().getStringExtra("class");
        value = getIntent().getIntExtra("value", -1);
        students = roll_list.size();
        attRecord = new boolean[students];

        ((TextView) findViewById(R.id.subject_text)).setText(getIntent().getStringExtra("subject"));
        ((TextView) findViewById(R.id.class_text)).setText(getIntent().getStringExtra("class"));
        rollNoTextView = (TextView) findViewById(R.id.rollNo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(students);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        toast = Toast.makeText(getApplicationContext(), roll_list.get(currentRollNo) +  " Present", Toast.LENGTH_SHORT);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        rollNoTextView.setText(roll_list.get(currentRollNo));

        ((Button) findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((Button) findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finalEditsActivity = new Intent(CountDown.this, Final_edits.class);
                finalEditsActivity.putExtra("roll_list", roll_list);
                finalEditsActivity.putExtra("attRecord", attRecord);
                finalEditsActivity.putExtra("subject_name", subject_name);
                finalEditsActivity.putExtra("class_name", class_name);
                finalEditsActivity.putExtra("value", value);
                startActivity(finalEditsActivity);
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(this.mDetector != null)
            this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_count_down, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            attRecord[currentRollNo] = true;
            if(toast != null)
                toast.cancel();
            toast.setText(roll_list.get(currentRollNo) + " Present");
            //todo: toast not getting shown in 4.x
            toast.show();
            progressBar.setProgress(currentRollNo + 1);
            ++currentRollNo;
            if(currentRollNo == students)
            {
                ((Button)findViewById(R.id.done)).setEnabled(true);
                rollNoTextView.setText("Complete");
                mDetector = null;
            }else
                rollNoTextView.setText(roll_list.get(currentRollNo));

            if (currentRollNo % 10 == 1)
                vibrator.vibrate(200);
            else
                vibrator.vibrate(100);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            if(Math.abs(event1.getX() - event2.getX()) < 75)
                return false;

            attRecord[currentRollNo] = false;
            if(toast != null)
                toast.cancel();
            toast.setText(roll_list.get(currentRollNo) + " Absent");
            toast.show();
            progressBar.setProgress(currentRollNo + 1);
            ++currentRollNo;
            if(currentRollNo == students)
            {
                ((Button)findViewById(R.id.done)).setEnabled(true);
                rollNoTextView.setText("Complete");
                mDetector = null;
            }else
                rollNoTextView.setText(roll_list.get(currentRollNo));

            if (currentRollNo % 10 == 1)
                vibrator.vibrate(200);
            else
                vibrator.vibrate(100);
            return true;
        }
    }
}
