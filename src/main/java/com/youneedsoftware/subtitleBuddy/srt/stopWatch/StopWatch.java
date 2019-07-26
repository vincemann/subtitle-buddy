package com.youneedsoftware.subtitleBuddy.srt.stopWatch;

public interface StopWatch {

    public void start();

    /**
     *
     * @param startTime in nanoseconds
     */
    public void start(long startTime);

    public void stop();

    public void resume();

    public void suspend();

    public void reset();

    /**
     *
     * @return  current time in nanoseconds
     */
    public long getTime();

    public RunningState getCurrentState();

}
