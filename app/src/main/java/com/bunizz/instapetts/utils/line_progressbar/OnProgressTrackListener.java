package com.bunizz.instapetts.utils.line_progressbar;

public interface OnProgressTrackListener {
    public void onProgressFinish();

    public void onProgressUpdate(int progress);
}
