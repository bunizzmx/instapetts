package com.bunizz.instapetts.utils.double_tap;

import android.util.Log;

public class SingleTapThread extends Thread {
    private long pressedTime;
    private int PRESS_TIME_GAP;
    private boolean isDoubleTapped;
    private DoubleTapLikeView.OnTapListener mListener;

    public SingleTapThread(long pressedTime, int pressTimeGap, boolean isDoubleTapped, DoubleTapLikeView.OnTapListener listener) {
        super();

        this.pressedTime    = pressedTime;
        this.PRESS_TIME_GAP = pressTimeGap;
        this.isDoubleTapped = isDoubleTapped;
        this.mListener      = listener;
    }

    @Override
    public void run() {
        while( !isInterrupted() ) {
            if( pressedTime +PRESS_TIME_GAP <= System.currentTimeMillis() ) {
                if( !isDoubleTapped ) {

                    interrupt();
                }
            }
        }
    }
}