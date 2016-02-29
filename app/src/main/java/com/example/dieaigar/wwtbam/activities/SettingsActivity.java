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
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dieaigar.wwtbam.R;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.StringTokenizer;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        addPreferencesFromResource(R.xml.preferences_settings);



    }

    @Override
    protected void onPause() {
        EditText etName = (EditText)findViewById(R.id.etName);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("userName", etName.getText().toString());

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String help = (String) spinner.getSelectedItem();
        Log.d("DEBUG", help);
        editor.putString("help", help);

        editor.apply();

        super.onPause();
    }

    public void addFriend(View view){
        String username = ((EditText)findViewById(R.id.etName)).getText().toString();
        String friend = ((EditText)findViewById(R.id.etFriend)).getText().toString();

        if(!friend.equals("")){
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.setUsername(username);
            myAsyncTask.setFriendname(friend);

            myAsyncTask.execute();
        }

    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPrefs.getString("userName", getResources().getString(R.string.nameless));

        EditText etName = (EditText)findViewById(R.id.etName);
        etName.setText(name);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String help = sharedPrefs.getString("help", "1");
        spinner.setSelection(Integer.parseInt(help));


        super.onResume();
    }




    private class MyAsyncTask
            extends AsyncTask<Void, HashMap<String,String>, Void> {
        String username, friendname;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFriendname() {
            return friendname;
        }

        public void setFriendname(String friendname) {
            this.friendname = friendname;
        }

        public boolean isNetworkConnected(int network) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info;
            switch(network) {
                case 0:
                    info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    break;
                case 1:
                    info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    break;
                default:
                    info = manager.getActiveNetworkInfo();
                    break;
            }
            return ((info != null) && (info.isConnected()));
        }

        @Override
        protected Void doInBackground(Void... params) {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http");
            builder.authority("wwtbamandroid.appspot.com");
            builder.appendPath("rest");
            builder.appendPath("friends");
            String body = "name=" + username +"&friend_name=" + friendname;
            try {
                URL url = new URL(builder.build().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();
                // Get response
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(HashMap<String,String>... values){

        }
    }

}
