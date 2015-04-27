package com.maurya.diwakar.attendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Final_edits extends ActionBarActivity {

    ArrayList<String> roll_list;
    boolean[] attRecord;
    String subject_name;
    String class_name;
    int value;
    private UploadData mUploadTask = null;
    View mLoginFormView;
    View mProgressView;

    private List<FeedItem> feedItemList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_edits);
        mLoginFormView = findViewById(R.id.final_edits_form);
        mProgressView = findViewById(R.id.upload_progress);

        roll_list = getIntent().getStringArrayListExtra("roll_list");
        attRecord = getIntent().getBooleanArrayExtra("attRecord");
        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        value = getIntent().getIntExtra("value", -1);

        feedItemList = new ArrayList<FeedItem>();
        for(int i = 0; i<roll_list.size(); ++i)
            feedItemList.add(new FeedItem(attRecord[i], roll_list.get(i)));

        /* Initialize recycler view */
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyRecyclerAdapter(Final_edits.this, feedItemList, ((TextView)findViewById(R.id.brief_info)));
        mRecyclerView.setAdapter(adapter);

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void attemptUpload(View view)
    {
        if(mUploadTask != null)
            return;
        new AlertDialog.Builder(this)
                .setTitle("Continue")
                .setMessage(
                        "Are you sure you want to upload data to the server?")
                .setIcon(
                        getResources().getDrawable(
                                android.R.drawable.ic_dialog_alert))
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                showProgress(true);
                                mUploadTask = new UploadData();
                                mUploadTask.execute((Void) null);
                            }
                        })
                .setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //do something here
                            }
                        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_final_edits, menu);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UploadData extends AsyncTask<Void, Integer, Boolean> {

        ServerReplyData serverReplyData;

        UploadData() {
        }

        protected void onProgressUpdate(Integer... progress) {
            ((ProgressBar) findViewById(R.id.upload_progress)).setProgress(progress[0]);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String rawData = "";
            for(int i = 0; i<roll_list.size(); ++i)
                rawData += feedItemList.get(i).myAttRecord ? '1' : '0';
            ContentValues paramValues = new ContentValues();
            paramValues.put("attRecord", rawData);
            paramValues.put("value", String.valueOf(value));
            paramValues.put("class_name", class_name);
            paramValues.put("subject_name", subject_name);

            String url_upload = getResources().getString(R.string.upload_url);
            serverReplyData = (new JSONParser()).makeHttpRequest(url_upload, "POST", paramValues);
            JSONObject json = serverReplyData.jsonObject;
            if (serverReplyData.httpStatusCode != 200)
                return false;
            //publishProgress(50);
            // check for querySuccess tag
            try {
                int success = json.getInt("success");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mUploadTask = null;
            showProgress(false);
            if (serverReplyData.httpStatusCode != 200) {
                if (serverReplyData.httpStatusCode == -1)
                    Toast.makeText(getApplicationContext(), "Exception occurred: " + serverReplyData.exceptionMessage, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Error in communication with server: " + serverReplyData.httpStatusCode, Toast.LENGTH_LONG).show();
            } else if (success) {
                Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(Final_edits.this, "Not successful", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mUploadTask = null;
            showProgress(false);
        }
    }
}
