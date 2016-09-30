package com.example.randall.readit.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randall.readit.AnalyticsApplication;
import com.example.randall.readit.R;
import com.example.randall.readit.RxUtils;
import com.example.randall.readit.domain.ParcelableScript;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ScriptPlayerActivity extends AppCompatActivity {

    public static String SCRIPT_DESCRIPTION = "script_description";
    private ParcelableScript parcelableScript;

    private TextView scriptDescriptionView;
    private ScrollView scrollView;
    private FloatingActionButton playScriptFab;
    private Tracker tracker;

    private Subscription subscription;



    boolean keepScrolling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        keepScrolling = false;

        scriptDescriptionView = (TextView)findViewById(R.id.script_description);
        scrollView = (ScrollView) findViewById(R.id.script_player_scrollview);
        playScriptFab = (FloatingActionButton) findViewById(R.id.play_pause_fab);

        scriptDescriptionView.setText(getIntent().getStringExtra(SCRIPT_DESCRIPTION));

        playScriptFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscription == null) {
                    keepScrolling = true;
                    subscription = getObservable()
                            .subscribeOn(Schedulers.io())
                            .subscribe(getObserver());
                    playScriptFab.setImageResource(R.drawable.ic_pause_white_24dp);
                }
                else{
                    subscription = null;
                    keepScrolling = false;
                    playScriptFab.setImageResource(R.drawable.ic_play_white_24dp);
                }
            }
        });

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(subscription);
    }


    @Override
    public void onResume() {
        super.onResume();
        tracker.setScreenName("Image~" + getClass().getName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    private Observable<Boolean> getObservable() {
        return Observable.just(true).map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                scrollScript();
                return aBoolean;
            }
        });
    }
    private Observer<Boolean> getObserver() {
        return new Observer<Boolean>() {

            @Override
            public void onCompleted() {



            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ScriptPlayerActivity.this, R.string.error_script_player, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Boolean bool) {

            }
        };
    }



    private void scrollScript() {
        while(keepScrolling){
            try{
                Thread.sleep(20);
            } catch (InterruptedException e){
                Log.e(getClass().getSimpleName(), e.getMessage());
            }

            int scrollRate = 2;
            if(scrollView.canScrollVertically(scrollRate)){
                scrollView.scrollBy(0, scrollRate);
            } else {
                keepScrolling = false;
                playScriptFab.setImageResource(R.drawable.ic_play_white_24dp);
            }
        }
    }


}
