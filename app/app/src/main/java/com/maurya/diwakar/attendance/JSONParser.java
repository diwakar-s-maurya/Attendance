/**
 * Created by diwakar on 4/2/15.
 */
package com.maurya.diwakar.attendance;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by diwakar on 28/3/15.
 */
public class JSONParser {

    InputStream is = null;
    JSONArray jsonArray = null;
    String json = "";

    // constructor
    public JSONParser() {
    }

    // function get json from url
    // by making HTTP POST or GET method
    public ServerReplyData makeHttpRequest(String requestUrl, String method,
                                           ContentValues params) {
        ServerReplyData serverReplyData = new ServerReplyData();
        serverReplyData.httpStatusCode = -1;
        // Making HTTP request
        try {
            // check for request method
            if (method == "POST") {
                // request method is POST
                // defaultHttpClient
                URL url = new URL(requestUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                serverReplyData.httpStatusCode = conn.getResponseCode();
                if (serverReplyData.httpStatusCode == 200) {
                    is = conn.getInputStream();
                } else {
                    //Todo: add some error handling if any
                    return serverReplyData;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (serverReplyData.exceptionMessage.isEmpty())
                serverReplyData.exceptionMessage = e.getMessage();
            return serverReplyData;
        } catch (IOException e) {
            e.printStackTrace();
            if (serverReplyData.exceptionMessage.isEmpty())
                serverReplyData.exceptionMessage = e.getMessage();
                return serverReplyData;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            if (serverReplyData.exceptionMessage.isEmpty())
                serverReplyData.exceptionMessage = e.getMessage();
            return serverReplyData;
        }

        // try parse the string to a JSON object
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing class_subject_roll_map " + e.toString());
            if (serverReplyData.exceptionMessage.isEmpty())
                serverReplyData.exceptionMessage = e.getMessage();
            return serverReplyData;
        }

        serverReplyData.jsonArray = jsonArray;
        return serverReplyData;
    }

    private String getQuery(ContentValues paramsValues)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Set<Map.Entry<String, Object>> s = paramsValues.valueSet();
        Iterator itr = s.iterator();
        while (itr.hasNext()) {
            if (first)
                first = false;
            else
                result.append("&");

            Map.Entry entry = (Map.Entry) itr.next();
            String key = entry.getKey().toString();
            String value = (String) entry.getValue();

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value, "UTF-8"));
        }
        return result.toString();
    }
}