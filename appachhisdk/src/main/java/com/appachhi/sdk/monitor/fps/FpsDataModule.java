package com.appachhi.sdk.monitor.fps;

import android.view.Choreographer;

import androidx.annotation.Nullable;

import com.appachhi.sdk.BaseDataModule;

import java.util.concurrent.TimeUnit;

public class FpsDataModule extends BaseDataModule<Double> implements Choreographer.FrameCallback {
    private long startFrameTimeMillis;
    private int numFramesRendered;
    private int interval;
    private Choreographer choreographer;
    private double fps;

    public FpsDataModule(int interval) {
        this.interval = interval;
        choreographer = Choreographer.getInstance();
    }

    @Nullable
    @Override
    protected Double getData() {
        return fps;
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
        long currentFrameTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);

        if (startFrameTimeMillis > 0) {
            long duration = currentFrameTimeMillis - startFrameTimeMillis;
            numFramesRendered++;

            if (duration > interval) {
                fps = numFramesRendered * 1000f / duration;

                notifyObservers();

                startFrameTimeMillis = currentFrameTimeMillis;
                numFramesRendered = 0;
            }
        } else {
            startFrameTimeMillis = currentFrameTimeMillis;
        }
        choreographer.postFrameCallback(this);
    }
}
