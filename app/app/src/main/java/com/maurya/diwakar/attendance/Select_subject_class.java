package com.maurya.diwakar.attendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Select_subject_class extends ActionBarActivity {

    HashMap<String, ArrayList<mPair<String, ArrayList<String> >  > > class_subject_roll_map;
    String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject_class);
        class_subject_roll_map = (HashMap<String, ArrayList<mPair<String, ArrayList<String> >  > >) getIntent().getSerializableExtra("class_subject_roll_map");
        mUsername = getIntent().getStringExtra("username");

        /*
        todo: add a description why have you taken class_subject_roll map(they all may become independent of each other)
        */
        List<String> class_list = new ArrayList<String>();
        class_list.add("Select Class");
        String[] classes = class_subject_roll_map.keySet().toArray(new String[class_subject_roll_map.size()]);
        for(int i = 0; i<classes.length; ++i)
            class_list.add(classes[i]);

        Spinner class_spinner = (Spinner)findViewById(R.id.class_spinner);
        ArrayAdapter<String> class_Adapter = new ArrayAdapter<String>(Select_subject_class.this, R.layout.my_spinner, class_list);
        class_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        class_spinner.setAdapter(class_Adapter);

        final List<String> subject_list = new ArrayList<String>();
        subject_list.add(0, "Select Subject");
        final Spinner subject_spinner = (Spinner)findViewById(R.id.subject_spinner);
        ArrayAdapter<String> subject_Adapter = new ArrayAdapter<String>(Select_subject_class.this, R.layout.my_spinner, subject_list);
        subject_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject_spinner.setAdapter(subject_Adapter);

        class_spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Spinner subject_spinner = (Spinner)findViewById(R.id.subject_spinner);
                if(i == 0) //first item in class_spinner is getting clicked automatically when this activity starts
                {
                    subject_spinner.setEnabled(false);
                    subject_list.clear();
                    subject_list.add("Select Subject");
                    Toast.makeText(Select_subject_class.this, "Select a class first", Toast.LENGTH_SHORT).show();
                    return;
                }
                subject_list.clear();
                subject_spinner.setEnabled(true);
                String selectedClass = ((TextView)view).getText().toString();
                ArrayList<mPair<String, ArrayList<String> >  > classData = class_subject_roll_map.get(selectedClass);
                for(int j = 0; j<classData.size(); ++j)
                    subject_list.add(classData.get(j).first);
                subject_list.add(0, "Select Subject");

                //ArrayAdapter<String> subject_Adapter = new ArrayAdapter<String>(Select_subject_class.this, android.R.layout.simple_sp, subject_list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Spinner subject_spinner = (Spinner)findViewById(R.id.subject_spinner);
                subject_spinner.setEnabled(false);
            }
        });

        List<String> value_list = new ArrayList<>(3);
        for(int i = 1; i<=3; ++i)
            value_list.add(String.valueOf(i));
        Spinner value_spinner = (Spinner)findViewById(R.id.value_spinner);
        ArrayAdapter<String> value_Adapter = new ArrayAdapter<String>(this, R.layout.my_spinner, value_list);
        value_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        value_spinner.setAdapter(value_Adapter);
        value_spinner.setSelection(0);
   }

    public void onNextButtonClicked(View v) {
        Spinner class_spinner = (Spinner)findViewById(R.id.class_spinner);
        Spinner subject_spinner = (Spinner)findViewById(R.id.subject_spinner);
        if(class_spinner.getSelectedItemPosition() <= 0 || subject_spinner.getSelectedItemPosition() <= 0)
        {
            Toast.makeText(Select_subject_class.this, "Select class/subject", Toast.LENGTH_SHORT).show();
            return;
        }
        String subject_name = subject_spinner.getSelectedItem().toString();
        String class_name = class_spinner.getSelectedItem().toString();
        /*note: added this if because the app will get crashed if DBA does something fishy, and there are no items in the roll_list
        and we find the roll_list.size() later*/

        ArrayList<String> roll_list = null;

        ArrayList<mPair<String, ArrayList<String> >  > classData = class_subject_roll_map.get(class_name);
        for(int i = 0; i<classData.size(); ++i)
            if(classData.get(i).first == subject_name)
                if(classData.get(i).second.size() == 0) {
                    Toast.makeText(Select_subject_class.this, "No enrolled in this class:subject", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    roll_list = classData.get(i).second;
                    break;
                }

        Intent startCountDownActivity = new Intent(Select_subject_class.this, CountDown.class);
        startCountDownActivity.putExtra("subject", subject_name);
        startCountDownActivity.putExtra("class", class_name);
        startCountDownActivity.putExtra("value", Integer.valueOf(((Spinner)findViewById(R.id.value_spinner)).getSelectedItem().toString()).intValue());
        startCountDownActivity.putExtra("roll_list", roll_list);
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
