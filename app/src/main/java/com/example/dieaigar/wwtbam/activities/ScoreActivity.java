package com.example.dieaigar.wwtbam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.dieaigar.wwtbam.R;
import com.example.dieaigar.wwtbam.databases.SQLHelper;

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
        tvName.setText(name);

        TextView tvScore = new TextView(this);
        tvScore.setText(score + "");

        row.addView(tvName);
        row.addView(tvScore);

        tl.addView(row, friendRows++);
    }
}
