package com.maurya.diwakar.attendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Select_subject_class extends ActionBarActivity {

    HashMap<mPair<String, String>, ArrayList<String>> class_subject_roll_map;
    String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject_class);
        class_subject_roll_map = (HashMap<mPair<String, String>, ArrayList<String>>) getIntent().getSerializableExtra("class_subject_roll_map");
        mUsername = getIntent().getStringExtra("username");

        List<String> class_list = new ArrayList<String>();
        List<String> subject_list = new ArrayList<String>();
        class_list.add("Select Class");
        subject_list.add("Select Subject");
        Set<mPair<String, String>> set = class_subject_roll_map.keySet();
        Iterator it = set.iterator();
        while(it.hasNext())
        {
            mPair<String, String> key = (mPair<String, String>)it.next();
            class_list.add(key.first);
            subject_list.add(key.second);
        }

        Spinner class_spinner = (Spinner)findViewById(R.id.class_spinner);
        ArrayAdapter<String> class_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, class_list);
        class_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        class_spinner.setAdapter(class_Adapter);

        Spinner subject_spinner = (Spinner)findViewById(R.id.subject_spinner);
        ArrayAdapter<String> subject_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subject_list);
        subject_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject_spinner.setAdapter(subject_Adapter);

        List<String> value_list = new ArrayList<>(5);
        for(int i = 1; i<=5; ++i)
            value_list.add(String.valueOf(i));
        Spinner value_spinner = (Spinner)findViewById(R.id.value_spinner);
        ArrayAdapter<String> value_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, value_list);
        value_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        value_spinner.setAdapter(value_Adapter);
        value_spinner.setSelection(0);
   }

    public void onNextButtonClicked(View v) {
        Spinner class_spinner = (Spinner)findViewById(R.id.class_spinner);
        Spinner subject_spinner = (Spinner)findViewById(R.id.subject_spinner);
        if(class_spinner.getSelectedItemPosition() == 0 || subject_spinner.getSelectedItemPosition() == 0)
        {
            Toast.makeText(Select_subject_class.this, "Select class/subject", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent startCountDownActivity = new Intent(Select_subject_class.this, CountDown.class);
        String subject_name = subject_spinner.getSelectedItem().toString();
        String class_name = class_spinner.getSelectedItem().toString();
        startCountDownActivity.putExtra("subject", subject_name);
        startCountDownActivity.putExtra("class", class_name);
        startCountDownActivity.putExtra("value", ((Spinner)findViewById(R.id.value_spinner)).getSelectedItem().toString());
        startCountDownActivity.putExtra("roll", class_subject_roll_map.get(mPair.of(subject_name, class_name)));
        startCountDownActivity.putExtra("username", mUsername);
        startActivity(startCountDownActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_subject_class, menu);
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
}
