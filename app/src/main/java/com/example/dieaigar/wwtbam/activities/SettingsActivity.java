package com.example.dieaigar.wwtbam.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dieaigar.wwtbam.R;

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
}
