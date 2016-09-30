package com.example.randall.readit.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.randall.readit.R;
import com.example.randall.readit.data.ScriptsProvider;

/**
 * Created by Randall on 16/07/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class ReadItWidgetRemoteViewsService extends RemoteViewsService {

    /**
     * RemoteViewsService controlling the data being shown in the scrollable Football scores detail widget
     */



        public static String SCRIPT_INDEX = "script_index";


        // these indices must match the projection
        private static final int INDEX_SCRIPT_ID = 0;
        private static final int INDEX_SCRIPT_TITLE = 2;
        private static final int INDEX_SCRIPT_DESCRIPTION = 2;


        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {

            return new RemoteViewsFactory() {

                private Cursor data = null;

                @Override
                public void onCreate() {

                }

                @Override
                public void onDataSetChanged() {

                    if (data != null) {
                        data.close();
                    }
                    // This method is called by the app hosting the widget (e.g., the launcher)
                    // However, our ContentProvider is not exported so it doesn't have access to the
                    // data. Therefore we need to clear (and finally restore) the calling identity so
                    // that calls use our process and permission
                    final long identityToken = Binder.clearCallingIdentity();
                    // Get today's score from the ContentProvider
                    data = getContentResolver().query(ScriptsProvider.Scripts.CONTENT_URI, null,
                            null, null, null);
                    Binder.restoreCallingIdentity(identityToken);
                }

                @Override
                public void onDestroy() {
                    if (data != null) {
                        data.close();
                        data = null;
                    }
                }

                @Override
                public int getCount() {
                    return data == null ? 0 : data.getCount();
                }

                @Override
                public RemoteViews getViewAt(int position) {

                    if (position == AdapterView.INVALID_POSITION ||
                            data == null || !data.moveToPosition(position)) {
                        return null;
                    }

                    RemoteViews views = new RemoteViews(getPackageName(),
                            R.layout.widget_script_list_item);
                    // Extract the scores from the Cursor

                    String scriptTitle = data.getString(INDEX_SCRIPT_TITLE);

                    views.setTextViewText(R.id.widget_script_title, scriptTitle);

                    final Intent fillInIntent = new Intent();

                    fillInIntent.putExtra(SCRIPT_INDEX, position);
                    views.setOnClickFillInIntent(R.id.widget, fillInIntent);

                    return views;
                }

                @Override
                public RemoteViews getLoadingView() {
                    return new RemoteViews(getPackageName(), R.layout.widget_script_list_item);
                }

                @Override
                public int getViewTypeCount() {
                    return 1;
                }

                @Override
                public long getItemId(int position) {
                    if (data.moveToPosition(position))
                        return data.getLong(INDEX_SCRIPT_ID);
                    return position;
                }

                @Override
                public boolean hasStableIds() {
                    return true;
                }
            };
        }



}
