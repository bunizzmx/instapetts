package com.bunizz.instapetts.utils.AnimatedTextViews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

public class BlinkTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final String TAG = "BlinkTextView";
    private static final boolean DEBUG = false;

    private static void DEBUG_LOG(String msg) {
        if (DEBUG) {
            Log.v(TAG, msg);
        }
    }

    private static final int MIN_ALPHA = 0;
    private static final int MAX_ALPHA = 255;

    // options constant
    private static final int MIN_FADING_DURATION = 150; // ms
    private static final int DEFAULT_FADING_DURATION = 2000; // ms
    private static final int DEFAULT_DELAY_AFTER_FADEOUT = 300; // ms
    private static final int DEFAULT_DELAY_AFTER_FADEIN = 800; // ms

    // blink
    private ColorStateList mOriginalColors;
    private int mCurrentAlpha;

    // options
    private long mFadingDuration = DEFAULT_FADING_DURATION;
    private long mDelayAfterFadein = DEFAULT_DELAY_AFTER_FADEIN;
    private long mDelayAfterFadeout = DEFAULT_DELAY_AFTER_FADEOUT;

    private Scroller mScroller;
    private boolean mIsFadein;
    private boolean mBlinkStarted;

    private final Runnable mReverseFadingRunnable = new Runnable() {
        public void run() {
            mIsFadein = !mIsFadein;
            DEBUG_LOG("mReverseFadingRunnable mIsFadein=" + mIsFadein);
            startBlink();
        }
    };

    public BlinkTextView(Context context) {
        super(context);
        initBlinkTextView();
    }

    public BlinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBlinkTextView();
    }

    public BlinkTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBlinkTextView();
    }

    private void initBlinkTextView() {
        final Context context = getContext();
        mScroller = new Scroller(context, new LinearInterpolator());
        mCurrentAlpha = MAX_ALPHA;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        super.setEnabled(enabled);
        if (enabled) {
            startBlink();
        } else {
            stopBlink();
        }
    }

    public long getFadingDuration() {
        return mFadingDuration;
    }

    public void setFadingDuration(long fadingDuration) {
        if (fadingDuration < MIN_FADING_DURATION) {
            fadingDuration = MIN_FADING_DURATION;
        }
        if (mFadingDuration != fadingDuration) {
            mFadingDuration = fadingDuration;
            resetBlink();
        }
    }

    public long getDelayAfterFadein() {
        return mDelayAfterFadein;
    }

    public void setDelayAfterFadein(long delayAfterFadein) {
        if (delayAfterFadein < 0) {
            delayAfterFadein = 0;
        }
        if (mDelayAfterFadein != delayAfterFadein) {
            mDelayAfterFadein = delayAfterFadein;
            resetBlink();
        }
    }

    public long getDelayAfterFadeout() {
        return mDelayAfterFadeout;
    }

    public void setDelayAfterFadeout(long delayAfterFadeout) {
        if (delayAfterFadeout < 0) {
            delayAfterFadeout = 0;
        }
        if (mDelayAfterFadeout != delayAfterFadeout) {
            mDelayAfterFadeout = delayAfterFadeout;
            resetBlink();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startBlink();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopBlink();
        super.onDetachedFromWindow();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        DEBUG_LOG("onWindowFocusChanged hasWindowFocus=" + hasWindowFocus);
        if (hasWindowFocus) {
            startBlink();
        } else {
            stopBlink();
        }
    }

    @Override
    public void setTextAppearance(Context context, int resid) {
        super.setTextAppearance(context, resid);
        saveOriginalColor();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        saveOriginalColor();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        saveOriginalColor();
    }

    private void saveOriginalColor() {
        mOriginalColors = getTextColors().withAlpha(mCurrentAlpha);
    }

    private void resetBlink() {
        if (mBlinkStarted) {
            startBlink();
        }
    }

    private void startBlink() {
        if (!isEnabled()) {
            return;
        }
        int sx;
        int dx;
        if (mIsFadein) {
            sx = mCurrentAlpha;
            dx = MAX_ALPHA - mCurrentAlpha;
        } else {
            sx = mCurrentAlpha;
            dx = -mCurrentAlpha;
        }
        final int duration = (int) (mFadingDuration * Math.abs(dx) / MAX_ALPHA);
        DEBUG_LOG("startBlink mIsFadein=" + mIsFadein + ", mCurrentAlpha=" + mCurrentAlpha +
                ", sx=" + sx + ", dx=" + dx + ", duration=" + duration);
        mScroller.abortAnimation();
        mScroller.startScroll(sx, 0, dx, 0, duration);
        mBlinkStarted = true;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void stopBlink() {
        mScroller.abortAnimation();
        if (getHandler() != null) {
            getHandler().removeCallbacks(mReverseFadingRunnable);
        }
        mBlinkStarted = false;
    }

    @Override
    public void computeScroll() {
        if (!isEnabled() || !mBlinkStarted) {
            return;
        }
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            mCurrentAlpha = Math.max(MIN_ALPHA, Math.min(MAX_ALPHA, Math.abs(mScroller.getCurrX())));
            DEBUG_LOG("computeScroll mCurrentAlpha=" + mCurrentAlpha);
            super.setTextColor(mOriginalColors.withAlpha(mCurrentAlpha));

            // Keep on drawing until the animation has finished.
            ViewCompat.postInvalidateOnAnimation(this);
            return;
        }

        // Done with scroll, clean up state.
        stopBlink();
        final long delayMillis = mIsFadein ? mDelayAfterFadein : mDelayAfterFadeout;
        getHandler().postDelayed(mReverseFadingRunnable, delayMillis);
    }

}