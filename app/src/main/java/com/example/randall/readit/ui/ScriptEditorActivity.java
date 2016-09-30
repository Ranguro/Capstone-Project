package com.example.randall.readit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.randall.readit.R;

public class ScriptEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(ScriptEditorActivityFragment.ARG_SCRIPT_ID,
                    getIntent().getParcelableExtra(ScriptEditorActivityFragment.ARG_SCRIPT_ID));
            ScriptEditorActivityFragment fragment = new ScriptEditorActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_script_editor, menu);
        return true;
    }


    private void launchScriptPlayer() {
        Intent intent = new Intent(this,ScriptPlayerActivity.class);
        startActivity(intent);
    }

}
