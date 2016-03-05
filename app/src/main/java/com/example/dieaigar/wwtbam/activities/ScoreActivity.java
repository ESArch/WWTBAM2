package com.example.dieaigar.wwtbam.activities;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.dieaigar.wwtbam.R;
import com.example.dieaigar.wwtbam.databases.SQLHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreActivity extends AppCompatActivity {
    int localRows, friendRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("LOCALTAB");
        spec.setIndicator("Local");
        spec.setContent(R.id.tab_layout_included1);
        host.addTab(spec);

        spec = host.newTabSpec("FRIENDSTAB");
        spec.setIndicator("Friends");
        spec.setContent(R.id.tab_layout_included2);
        host.addTab(spec);

        friendRows = 0;
        localRows = 0;

        loadLocalScores();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        String name = sharedPrefs.getString("userName", getResources().getString(R.string.nameless));
        myAsyncTask.setUsername(name);
        myAsyncTask.execute();
    }

    public void loadLocalScores(){
        ArrayList<HashMap<String, String>> scores = SQLHelper.getInstance(this).getScores();
        for(int i=0; i<scores.size(); i++) {
            Log.d("DB DEBUG", scores.get(i).toString());
            addLocalScore(Integer.valueOf(scores.get(i).get("score")), scores.get(i).get("name"));
        }
    }

    private void addLocalScore(int score, String name){
        TableLayout tl = (TableLayout) findViewById(R.id.score_table_local);

        TableRow row = new TableRow(this);

        TextView tvName = new TextView(this);
        tvName.setText(name+" ");

        TextView tvScore = new TextView(this);
        tvScore.setText(score + "");

        row.addView(tvName);
        row.addView(tvScore);

        tl.addView(row, localRows++);

    }

    private void addFriendScore(int score, String name){
        TableLayout tl = (TableLayout) findViewById(R.id.score_table_friends);

        TableRow row = new TableRow(this);

        TextView tvName = new TextView(this);
        tvName.setText(name+" ");

        TextView tvScore = new TextView(this);
        tvScore.setText(score + "");

        row.addView(tvName);
        row.addView(tvScore);

        tl.addView(row, friendRows++);
    }

    private class MyAsyncTask
            extends AsyncTask<Void, HashMap<String,String>, Void> {
        String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isNetworkConnected() {
            // Get a reference to the ConnectivityManager
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            // Get information about the default active data network
            NetworkInfo info = manager.getActiveNetworkInfo();
            // There will be connectivity when there is a default connected network
            return ((info != null) && (info.isConnected()));
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isNetworkConnected()) {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http");
                builder.authority("wwtbamandroid.appspot.com");
                builder.appendPath("rest");
                builder.appendPath("highscores");
                builder.appendQueryParameter("name", username);
                String body = "name=" + username;
                try {
                    URL url = new URL(builder.build().toString());
                    Log.d("WS DEBUG", "Request: " + url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    Log.d("WS DEBUG", "Response code: " + connection.getResponseCode());
                    BufferedReader reader = null;
                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sBuilder = new StringBuilder();
                        String s;
                        while ((s = reader.readLine()) != null) {
                            sBuilder.append(s);
                        }
                        Log.d("WS DEBUG", "Response message: " + sBuilder.toString());
                        JSONObject jsonObj = new JSONObject(sBuilder.toString());
                        JSONArray scores = jsonObj.getJSONArray("scores");

                        for(int i = 0; i < scores.length(); i++) {
                            addFriendScore(Integer.parseInt(scores.getJSONObject(i).get("scoring").toString()), scores.getJSONObject(i).get("name").toString());
                        }
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(HashMap<String, String>... values) {

        }
    }
}
