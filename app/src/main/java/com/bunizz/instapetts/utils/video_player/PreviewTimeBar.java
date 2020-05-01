/*
 * Copyright 2017 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bunizz.instapetts.utils.video_player;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bunizz.instapetts.R;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.TimeBar;

import java.util.ArrayList;
import java.util.List;

public class PreviewTimeBar extends DefaultTimeBar implements PreviewView,
        TimeBar.OnScrubListener {

    private List<OnPreviewChangeListener> listeners;
    private PreviewDelegate delegate;
    private int scrubProgress;
    private int duration;
    private int scrubberColor;
    private int frameLayoutId;
    private int scrubberDiameter;

    public PreviewTimeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        listeners = new ArrayList<>();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                com.google.android.exoplayer2.ui.R.styleable.DefaultTimeBar, 0, 0);
        final int playedColor = a.getInt(
                com.google.android.exoplayer2.ui.R.styleable.DefaultTimeBar_played_color,
                DEFAULT_PLAYED_COLOR);
        scrubberColor = a.getInt(
                com.google.android.exoplayer2.ui.R.styleable.DefaultTimeBar_scrubber_color,
                getDefaultScrubberColor(playedColor));

        int defaultScrubberDraggedSize = dpToPx(context.getResources().getDisplayMetrics(),
                DEFAULT_SCRUBBER_DRAGGED_SIZE_DP);

        scrubberDiameter = a.getDimensionPixelSize(
                com.google.android.exoplayer2.ui.R.styleable.DefaultTimeBar_scrubber_dragged_size,
                defaultScrubberDraggedSize);

        a.recycle();

        a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PreviewSeekBar, 0, 0);
        frameLayoutId = a.getResourceId(R.styleable.PreviewSeekBar_previewFrameLayout, View.NO_ID);

        delegate = new PreviewDelegate(this, scrubberColor);
        delegate.setEnabled(isEnabled());
        addListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!delegate.isSetup() && getWidth() != 0 && getHeight() != 0 && !isInEditMode()) {
            delegate.onLayout((ViewGroup) getParent(), frameLayoutId);
        }
    }

    @Override
    public void setPreviewColorTint(int color) {
        delegate.setPreviewColorTint(color);
    }

    @Override
    public void setPreviewColorResourceTint(int color) {
        delegate.setPreviewColorResourceTint(color);
    }

    @Override
    public void setPreviewLoader(PreviewLoader previewLoader) {
        delegate.setPreviewLoader(previewLoader);
    }

    @Override
    public void attachPreviewFrameLayout(FrameLayout frameLayout) {
        delegate.attachPreviewFrameLayout(frameLayout);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        delegate.setEnabled(enabled);
    }

    @Override
    public void setDuration(long duration) {
        super.setDuration(duration);
        this.duration = (int) duration;
    }

    @Override
    public void setPosition(long position) {
        super.setPosition(position);
        this.scrubProgress = (int) position;
    }

    @Override
    public boolean isShowingPreview() {
        return delegate.isShowing();
    }

    @Override
    public void showPreview() {
        if (isEnabled()) {
            delegate.show();
        }
    }

    @Override
    public void hidePreview() {
        if (isEnabled()) {
            delegate.hide();
        }
    }

    @Override
    public int getProgress() {
        return scrubProgress;
    }

    @Override
    public int getMax() {
        return duration;
    }

    @Override
    public int getThumbOffset() {
        return scrubberDiameter / 2;
    }

    @Override
    public int getDefaultColor() {
        return scrubberColor;
    }

    @Override
    public void addOnPreviewChangeListener(OnPreviewChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeOnPreviewChangeListener(OnPreviewChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onScrubStart(TimeBar timeBar, long position) {
        for (OnPreviewChangeListener listener : listeners) {
            scrubProgress = (int) position;
            listener.onStartPreview(this, (int) position);
        }
    }

    @Override
    public void onScrubMove(TimeBar timeBar, long position) {
        for (OnPreviewChangeListener listener : listeners) {
            scrubProgress = (int) position;
            listener.onPreview(this, (int) position, true);
        }
    }

    @Override
    public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
        for (OnPreviewChangeListener listener : listeners) {
            listener.onStopPreview(this, (int) position);
        }
    }

    private int dpToPx(DisplayMetrics displayMetrics, int dps) {
        return (int) (dps * displayMetrics.density + 0.5f);
    }
}
