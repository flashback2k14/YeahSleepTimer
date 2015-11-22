package com.yeahdev.yeahsleeptimerfree.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yeahdev.yeahsleeptimerfree.R;
import com.yeahdev.yeahsleeptimerfree.fragments.PrefsFragment;

public class PrefsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPrefs);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, PrefsFragment.newInstance())
            .commit();
    }
}
