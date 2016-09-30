package com.example.randall.readit.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.randall.readit.AnalyticsApplication;
import com.example.randall.readit.R;
import com.example.randall.readit.data.ScriptColumns;
import com.example.randall.readit.data.ScriptsProvider;
import com.example.randall.readit.domain.ParcelableScript;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScriptEditorActivityFragment extends Fragment {

    public static String ARG_SCRIPT_ID = "script_id";

    private ParcelableScript parcelableScript;

    private EditText scriptTitle;

    private EditText scriptDescription;

    private InterstitialAd interstitialAd;

    private Tracker tracker;

    public ScriptEditorActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments().containsKey(ARG_SCRIPT_ID)) {
            parcelableScript = getArguments().getParcelable(ARG_SCRIPT_ID);
        }
        AnalyticsApplication application = (AnalyticsApplication) this.getActivity().getApplication();
        tracker = application.getDefaultTracker();

        interstitialAd = new InterstitialAd(getActivity().getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                launchScriptPlayer();
            }
        });

        requestNewInterstitial();
    }

    @Override
    public void onResume() {
        super.onResume();
        tracker.setScreenName("Image~" + getClass().getName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_script_editor, container, false);

        scriptTitle = (EditText) rootView.findViewById(R.id.script_title_field);
        scriptDescription = (EditText) rootView.findViewById(R.id.script_description_field);

        if (parcelableScript != null){
            scriptTitle.setText(parcelableScript.getTitle());
            scriptDescription.setText(parcelableScript.getDescription());
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play:
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    launchScriptPlayer();
                }
                return true;
            default:
                break;
        }

        return false;
    }

    private void launchScriptPlayer() {
        final Context appContext = getActivity().getApplicationContext();

        if (parcelableScript == null){
            ContentValues cv = new ContentValues();
            cv.put(ScriptColumns.TITLE, scriptTitle.getText().toString());
            cv.put(ScriptColumns.DESCRIPTION, scriptDescription.getText().toString());
            appContext.getContentResolver().insert(ScriptsProvider.Scripts.CONTENT_URI, cv);

        } else {
            ContentValues cv = new ContentValues();
            cv.put(ScriptColumns.TITLE, scriptTitle.getText().toString());
            cv.put(ScriptColumns.DESCRIPTION, scriptDescription.getText().toString());
            appContext.getContentResolver().update(ScriptsProvider.Scripts.CONTENT_URI, cv, ScriptColumns._ID + "=" + parcelableScript.getId(), null);
        }

            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(getString(R.string.action))
                    .setAction(getString(R.string.display_script))
                    .build());


            interstitialAd.show();
            requestNewInterstitial();
            Intent intent = new Intent(getActivity(),ScriptPlayerActivity.class);
            intent.putExtra(ScriptPlayerActivity.SCRIPT_DESCRIPTION,scriptDescription.getText().toString());
            startActivity(intent);

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        interstitialAd.loadAd(adRequest);

    }

}
