package com.maurya.diwakar.attendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A login screen that offers login via email/password.
 */
public class Authenticate extends ActionBarActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.username) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.isEmpty()) {
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            return;
        }

/*        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        //todo: not connecting to usb tethering, fix it
        boolean isConnected = false;
        for (int i = 0; i < info.length; ++i)
            if (info[i].isConnected()) {
                isConnected = true;
                break;
            }
        if (!isConnected) {
            Toast.makeText(Authenticate.this, "Not Connected to any network", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Integer, Boolean> {

        private final String mUsername;
        private final String mPassword;
        ServerReplyData serverReplyData;
        HashMap<mPair<String, String>, ArrayList<String>> class_subject_roll_map;
        private int querySuccess = -1;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        protected void onProgressUpdate(Integer... progress) {
            ((ProgressBar) findViewById(R.id.login_progress)).setProgress(progress[0]);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ContentValues paramValues = new ContentValues();
            paramValues.put("username", mUsername);
            paramValues.put("password", mPassword);

            String url_authenticate = getResources().getString(R.string.authenticate_url);
            serverReplyData = (new JSONParser()).makeHttpRequest(url_authenticate, "POST", paramValues);
            JSONArray json = serverReplyData.jsonArray;
            if (serverReplyData.httpStatusCode != 200)
                return false;
            publishProgress(50);
            // check for querySuccess tag
            try {
                // check log cat from response
                class_subject_roll_map = new HashMap<mPair<String, String>, ArrayList<String>>();
                Log.d("Upload Response", json.toString());
                querySuccess = json.getJSONObject(json.length() - 1).getInt("success");
                if (querySuccess != 1)
                    return false;
                for (int i = 0; i < json.length() - 1; ++i) {
                    JSONObject row = json.getJSONObject(i);
                    mPair<String, String> stringStringPair = mPair.of(row.getString("class"), row.getString("subject"));
                    ArrayList<String> rollList = new ArrayList<String>();
                    JSONArray rolls = row.getJSONArray("roll");
                    for (int j = 0; j < rolls.length(); ++j)
                        rollList.add(rolls.get(j).toString());
                    class_subject_roll_map.put(stringStringPair, rollList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (serverReplyData.httpStatusCode != 200) {
                if (serverReplyData.httpStatusCode == -1)
                    Toast.makeText(getApplicationContext(), "Exception occurred: " + serverReplyData.exceptionMessage, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Error in communicating with server: " + serverReplyData.httpStatusCode, Toast.LENGTH_LONG).show();
            } else if (querySuccess == 1) {
                super.onPostExecute(success);
                finish();
                Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Authenticate.this, Select_subject_class.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("class_subject_roll_map", class_subject_roll_map);
                intent.putExtra("username", mUsername);
                Authenticate.this.startActivity(intent);
                return;
            } else if (querySuccess == 2) {
                Toast.makeText(getApplicationContext(), "Wrong username/password combination", Toast.LENGTH_SHORT).show();
                mPasswordView.setError(getString(R.string.error_username_password_combo));
            }
            mPasswordView.requestFocus();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
