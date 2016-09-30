package com.example.randall.readit.adapters;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.randall.readit.R;
import com.example.randall.readit.data.ScriptColumns;


/**
 * Credit to skyfishjy gist:
 *    https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */

public class ScriptCursorAdapter extends CursorRecyclerViewAdapter<ScriptCursorAdapter.ViewHolder> {
    Context mContext;
    ViewHolder mVh;
    private Cursor mCursor;

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ScriptCursorAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
        mCursor = cursor;
    }

    public interface ClickListener {

        void onScriptSelected(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;

        public ViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.script_title);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (clickListener == null)
                return;
            clickListener.onScriptSelected(view, adapterPosition);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.script_list_content, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor){
        DatabaseUtils.dumpCursor(cursor);
        viewHolder.mTextView.setText(cursor.getString(
                cursor.getColumnIndex(ScriptColumns.TITLE)));

    }

}