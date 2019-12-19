package com.appachhi.sdk.monitor.framedrop;

import android.content.Context;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import com.appachhi.sdk.BaseDataModule;

public class FrameDropDataModule extends BaseDataModule<Long> implements Choreographer.FrameCallback {
    private Choreographer choreographer;
    private long frameDropped;
    private long frameIntervalNanos;
    private int skippedFrameNotifyLimit;

    FrameDropDataModule(Context context, int skippedFrameNotifyLimit) {
        choreographer = Choreographer.getInstance();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        frameIntervalNanos = (long) (1000000000 / display.getRefreshRate());
        this.skippedFrameNotifyLimit = skippedFrameNotifyLimit;

    }


    @Override
    protected Long getData() {
        return frameDropped;
    }

    @Override
    public void start() {
        Log.d("FrameDropData","start");
        choreographer.postFrameCallback(this);
    }

    @Override
    public void stop() {
        choreographer.removeFrameCallback(this);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        Log.d("FrameDropData","Do Frame");
        final long startNanos = System.nanoTime();
        final long jitterNanos = startNanos - frameTimeNanos;
        if (jitterNanos >= frameTimeNanos) {
            final long skippedFrames = jitterNanos / frameIntervalNanos;
            if (skippedFrames >= skippedFrameNotifyLimit) {
                frameDropped = skippedFrames;
                notifyObservers();
            }
        }
        choreographer.postFrameCallback(this);
    }
}