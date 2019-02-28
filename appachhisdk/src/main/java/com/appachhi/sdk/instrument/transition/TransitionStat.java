package com.appachhi.sdk.instrument.transition;

import android.os.SystemClock;

/**
 * Class holding information about the start and end time of the transition.
 * It also keep information about whether the information has been flushed or not
 */
final class TransitionStat {
    // Id  of the screen transitioned
    private int id;
    // Start time of the transition in milliseconds
    private long startTime;
    // End time of the transition in millis seconds
    private long endTime = -1L;
    // Whether the transition information has been flushed or not
    private boolean flushed = false;

    private TransitionStat(int id, long startTime, long endTime, boolean flushed) {
        this(id, startTime);
        this.endTime = endTime;
        this.flushed = flushed;
    }

    private TransitionStat(int id, long startTime) {
        this.id = id;
        this.startTime = startTime;
    }

     int getId() {
        return id;
    }

    long getStartTime() {
        return startTime;
    }

    Long getEndTime() {
        return endTime;
    }

    boolean isFlushed() {
        return flushed;
    }

    /**
     * Return the duration of the transiton.The caller should only call this method once the transition has ended.
     * Calling this before transition has ended will result in {@link IllegalStateException}
     *
     * @return Duration of the screen transition
     */
    @SuppressWarnings("JavadocReference")
    long transitionDuration() throws IllegalStateException {
        if (endTime == -1) {
            throw new IllegalStateException("Cannot compute duration for unfinished transition");
        }
        return endTime - startTime;
    }

    /**
     * Create  a new instance of {@link TransitionStat} for a given id and startTime.
     *
     * @param id        Id of the transition
     * @param startTime Start Time of the transition
     * @return TransitionStat
     */
    private static TransitionStat beginTransitionStat(int id, long startTime) {
        return new TransitionStat(id, startTime);
    }

    /**
     * Create  a new instance of {@link TransitionStat} for a given id. The start time is taken by System Clock which is
     * upto Nano Seconds Precision
     *
     * @param id Id of the transition
     * @return TransitionStat
     */
    static TransitionStat beginTransitionStat(int id) {
        return beginTransitionStat(id, SystemClock.elapsedRealtime());
    }

    TransitionStat copy(long endTime) {
        return new TransitionStat(id, startTime, endTime, true);
    }
}
