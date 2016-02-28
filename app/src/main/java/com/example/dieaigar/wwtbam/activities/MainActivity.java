package com.example.dieaigar.wwtbam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dieaigar.wwtbam.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchActivity(View view){
        Intent intent = null;
        switch(view.getId()) {
            case R.id.bPlay:
                intent = new Intent(this, PlayActivity.class);
                break;
            case R.id.bScore:
                intent = new Intent(this, ScoreActivity.class);
                break;
            case R.id.bSettings:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }

        if(intent != null)
            startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.menu_credits:
                intent = new Intent(this, CreditsActivity.class);

                break;
        }

        if(intent != null)
            startActivity(intent);

        supportInvalidateOptionsMenu();
        return true;
    }
}
