package com.example.randall.readit.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.randall.readit.AnalyticsApplication;
import com.example.randall.readit.R;
import com.example.randall.readit.adapters.ScriptCursorAdapter;
import com.example.randall.readit.data.ScriptColumns;
import com.example.randall.readit.data.ScriptsProvider;
import com.example.randall.readit.domain.ParcelableScript;
import com.example.randall.readit.widget.ReadItWidgetRemoteViewsService;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ScriptCursorAdapter.ClickListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int CURSOR_LOADER_ID = 0;
    private RecyclerView scriptsRecyclerView;
    private ScriptCursorAdapter cursorAdapter;
    private Tracker tracker;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scriptsRecyclerView = (RecyclerView) findViewById(R.id.script_list);
        assert scriptsRecyclerView != null;

        scriptsRecyclerView.setLayoutManager(new LinearLayoutManager(scriptsRecyclerView.getContext()));

        cursorAdapter = new ScriptCursorAdapter(this, null);
        scriptsRecyclerView.setAdapter(cursorAdapter);
        cursorAdapter.setClickListener(this);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        if (getIntent().hasExtra(ReadItWidgetRemoteViewsService.SCRIPT_INDEX)){
            int selectedWidgetMatch = getIntent().getExtras().
                    getInt(ReadItWidgetRemoteViewsService.SCRIPT_INDEX);
            scriptsRecyclerView.scrollToPosition(selectedWidgetMatch);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void launchScriptEditorActivity(View view) {
        Intent intent = new Intent(this,ScriptEditorActivity.class);
        startActivity(intent);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.add_new_script))
                .build());
    }




    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID,null,this);

        Log.i(LOG_TAG, "Setting screen name: " + getClass().getName());
        tracker.setScreenName("Image~" +  getClass().getName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ScriptsProvider.Scripts.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (cursorAdapter == null){
            cursorAdapter = new ScriptCursorAdapter(this, data);
            scriptsRecyclerView.setAdapter(cursorAdapter);

        } else {
            cursorAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (cursorAdapter != null){
            cursorAdapter.swapCursor(null);
        }
    }


    @Override
    public void onScriptSelected(View view, int position) {

        Cursor mCursor = cursorAdapter.getCursor();

        if(mCursor.moveToPosition(position)) {
            ParcelableScript parcelableScript =
                    new ParcelableScript(mCursor.getInt(mCursor.getColumnIndex(ScriptColumns._ID)),
                            mCursor.getString(1),
                            mCursor.getString(2));

            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(getString(R.string.action))
                    .setAction(getString(R.string.edit_saved_script))
                    .build());

            Intent intent = new Intent(this,ScriptEditorActivity.class);
            intent.putExtra(ScriptEditorActivityFragment.ARG_SCRIPT_ID, parcelableScript);
            startActivity(intent);

        }
    }
}
