package com.example.dieaigar.wwtbam.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.dieaigar.wwtbam.R;
import com.example.dieaigar.wwtbam.databases.SQLHelper;
import com.example.dieaigar.wwtbam.pojo.Question;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class PlayActivity extends AppCompatActivity {

    TextView tvQuestion, tvQuestionNumber, tvPrize;
    Button bAnswer1, bAnswer2, bAnswer3, bAnswer4;
    int right, audience, phone, fifty1, fifty2, current, usedHelps, maxHelps;
    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvQuestionNumber = (TextView) findViewById(R.id.tvQuestionNumber);
        tvPrize = (TextView) findViewById(R.id.tvPrize);
        bAnswer1 = (Button) findViewById(R.id.bAnswer1);
        bAnswer2 = (Button) findViewById(R.id.bAnswer2);
        bAnswer3 = (Button) findViewById(R.id.bAnswer3);
        bAnswer4 = (Button) findViewById(R.id.bAnswer4);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        maxHelps = Integer.valueOf(sharedPrefs.getString("help", "1"));




    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("usedHelps", usedHelps);
        editor.putInt("currentQuestion", current);
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        current = sharedPrefs.getInt("currentQuestion", 1);
        usedHelps = sharedPrefs.getInt("usedHelps", 0);
        loadQuestion(current);
        super.onResume();
    }

    public void checkAnswer(View view){
        current++;
        if(isRightAnswer(view) && current < 16)
            loadQuestion(current);
        else if (isRightAnswer(view) && current == 16) {
            win();
        } else {
            fail();
        }

    }

    private boolean isRightAnswer(View view){
        if(right == 1 && view.equals(bAnswer1))
            return true;
        else if (right == 2 && view.equals(bAnswer2))
            return true;
        else if (right == 3 && view.equals(bAnswer3))
            return true;
        else if (right == 4 && view.equals(bAnswer4))
            return true;
        else {
            return false;
        }
    }

    private void loadQuestion(int index){

        tvQuestionNumber.setText(R.string.pregunta + current);
        tvPrize.setText(R.string.en_juego + computeScore(current) + " " + getResources().getString(R.string.currency));
        clearAnswer();
        showAnswers();

        try{
//            FileInputStream fis = openFileInput(getResources().getXml(R.xml.questions));
//            InputStreamReader reader = new InputStreamReader(fis);
//            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            XmlPullParser parser = getResources().getXml(R.xml.questions);
//            parser.setInput(reader);
            int eventType = parser.getEventType();
            EditText target = null;

            while (XmlPullParser.END_DOCUMENT != eventType) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("question") && Integer.valueOf(parser.getAttributeValue(null, "number")) == index){
                            tvQuestion.setText(parser.getAttributeValue(null, "text"));
                            bAnswer1.setText(parser.getAttributeValue(null, "answer1"));
                            bAnswer2.setText(parser.getAttributeValue(null, "answer2"));
                            bAnswer3.setText(parser.getAttributeValue(null, "answer3"));
                            bAnswer4.setText(parser.getAttributeValue(null, "answer4"));

                            right = Integer.valueOf(parser.getAttributeValue(null, "right"));
                            audience = Integer.valueOf(parser.getAttributeValue(null, "audience"));
                            phone = Integer.valueOf(parser.getAttributeValue(null, "phone"));
                            fifty1 = Integer.valueOf(parser.getAttributeValue(null, "fifty1"));
                            fifty2 = Integer.valueOf(parser.getAttributeValue(null, "fifty2"));
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                }
                parser.next();
                eventType = parser.getEventType();
            }
//            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_abanonar:
                    quit();
                break;
            case R.id.menu_publico:
                if(usedHelps < maxHelps){
                    displayRightAnswer(audience);
                    usedHelps++;
                }else {
                    Toast.makeText(PlayActivity.this, "You can't use help anymore", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_llamada:
                if(usedHelps < maxHelps){
                    displayRightAnswer(phone);
                    usedHelps++;
                }else {
                    Toast.makeText(PlayActivity.this, "You can't use help anymore", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_cincuenta:
                if(usedHelps < maxHelps){
                    displayWrongAnswer(fifty1, fifty2);
                    usedHelps++;
                }else {
                    Toast.makeText(PlayActivity.this, "You can't use help anymore", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        supportInvalidateOptionsMenu();
        return true;
    }

    private void clearAnswer(){
        bAnswer1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bAnswer2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bAnswer3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bAnswer4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    private void hideAnswers(){
        bAnswer1.setVisibility(View.GONE);
        bAnswer2.setVisibility(View.GONE);
        bAnswer3.setVisibility(View.GONE);
        bAnswer4.setVisibility(View.GONE);
    }

    private void showAnswers(){
        bAnswer1.setVisibility(View.VISIBLE);
        bAnswer2.setVisibility(View.VISIBLE);
        bAnswer3.setVisibility(View.VISIBLE);
        bAnswer4.setVisibility(View.VISIBLE);
    }

    private void displayRightAnswer(int answer){
        clearAnswer();

        switch (answer){
            case 1:
                bAnswer1.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
                break;
            case 2:
                bAnswer2.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
                break;
            case 3:
                bAnswer3.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
                break;
            case 4:
                bAnswer4.setBackgroundColor(getResources().getColor(R.color.colorLightPrimary));
                break;
        }

    }

    private void displayWrongAnswer(Integer ... answers){
        clearAnswer();
        for(Integer answer : answers){
            switch (answer){
                case 1:
                    bAnswer1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    break;
                case 2:
                    bAnswer2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    break;
                case 3:
                    bAnswer3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    break;
                case 4:
                    bAnswer4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    break;
            }
        }
    }

    private void quit(){
        score = computeScore(current-1);
        tvQuestion.setText(R.string.abandonar + "\n" + R.string.recompensa + score);
        hideAnswers();
        leave();
    }

    private void fail(){
        score = computeSavedScore(current-2);
        tvQuestion.setText(R.string.fallo + "\n" + R.string.recompensa + score);
                hideAnswers();
        leave();
    }

    private void win(){
        score = computeScore(current-1);
        tvQuestion.setText(R.string.victoria + "\n" + R.string.recompensa + score);
                hideAnswers();
        leave();
    }

    private void leave(){
        current = 1;
        usedHelps = 0;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPrefs.getString("userName", getResources().getString(R.string.nameless));

        //Save score in the local DB
        SQLHelper.getInstance(this).addScore(name, score);
        //Publish the score in the web service
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.setUsername(name);
        myAsyncTask.setScore(score);
        myAsyncTask.execute();


    }

    private String computeScore(int current){
        switch(current){
            case 0: return "0";
            case 1: return "100";
            case 2: return "200";
            case 3: return "300";
            case 4: return "500";
            case 5: return "1000";
            case 6: return "2000";
            case 7: return "4000";
            case 8: return "8000";
            case 9: return "16000";
            case 10: return "32000";
            case 11: return "64000";
            case 12: return "125000";
            case 13: return "250000";
            case 14: return "500000";
            case 15: return "1000000";
        }
        return "";
    }

    private String computeSavedScore(int current){
        switch(current){
            case 0: ;
            case 1: ;
            case 2: ;
            case 3: ;
            case 4: return "0";
            case 5: ;
            case 6: ;
            case 7: ;
            case 8: ;
            case 9: return "1000";
            case 10: ;
            case 11: ;
            case 12: ;
            case 13: ;
            case 14: ;
            case 15: return "32000";
        }
        return "";
    }

    private class MyAsyncTask
            extends AsyncTask<Void, HashMap<String,String>, Void> {
        String username, score;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
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
                String body = "name=" + username + "&score=" + score;
                try {
                    URL url = new URL(builder.build().toString());
                    Log.d("WS DEBUG", "Request: " + url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(body);
                    writer.flush();
                    writer.close();
                    // Get response
                    Log.d("WS DEBUG", "Response code: " + connection.getResponseCode());
                    connection.disconnect();
                } catch (IOException e) {
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
