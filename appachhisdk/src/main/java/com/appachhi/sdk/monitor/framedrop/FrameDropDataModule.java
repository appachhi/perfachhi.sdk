package com.appachhi.sdk.monitor.framedrop;

import android.content.Context;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import com.appachhi.sdk.BaseDataModule;

public class FrameDropDataModule extends BaseDataModule<Long> implements Choreographer.FrameCallback {
    private Choreographer choreographer;
    private long frameDropped;
    private long frameIntervalNanos;
    private int skippedFrameNotifyLimit;
    private long lastFrameRendered;

    FrameDropDataModule(Context context, int skippedFrameNotifyLimit) {
        choreographer = Choreographer.getInstance();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        frameIntervalNanos = (long) (1 / display.getRefreshRate() * 1000000000);
        this.skippedFrameNotifyLimit = skippedFrameNotifyLimit;

    }


    @Override
    protected Long getData() {
        return frameDropped;
    }

    @Override
    public void start() {
        choreographer.postFrameCallback(this);
    }

    @Override
    public void stop() {
        choreographer.removeFrameCallback(this);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        final long now = System.nanoTime();
        if (lastFrameRendered != 0L) {
            final long differentBetweenLastFrameRenderAndNow = System.nanoTime() - lastFrameRendered;
            if (differentBetweenLastFrameRenderAndNow >= frameIntervalNanos) {
                final long skippedFrames = differentBetweenLastFrameRenderAndNow / frameIntervalNanos;
                if (skippedFrames >= skippedFrameNotifyLimit) {
                    frameDropped = skippedFrames;
                    notifyObservers();
                }
            }

        }
        lastFrameRendered = frameTimeNanos;
        choreographer.postFrameCallback(this);
    }

}