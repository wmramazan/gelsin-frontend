package com.gelsin.android.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        Log.d("TOUCH", "Constructed");

        this.clickListener = clickListener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d("TOUCH", "onSingleTapUp: " + e);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(child != null && clickListener != null){
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
                Log.d("TOUCH", "onLongPress: " + e);
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){

            clickListener.onClick(child, rv.getChildPosition(child));

        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d("TOUCH", "onTouchEvent: " + e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d("TOUCH", "onRequestDisallowInterceptTouchEvent: " + (disallowIntercept?"1":"0"));
    }

    public static interface ClickListener{

        public void onClick(View view, int position);

        public void onLongClick(View view, int position);

    }
}