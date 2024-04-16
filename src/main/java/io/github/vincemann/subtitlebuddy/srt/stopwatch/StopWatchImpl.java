package io.github.vincemann.subtitlebuddy.srt.stopwatch;


import com.google.inject.Singleton;

@Singleton
public class StopWatchImpl implements StopWatch {


    private static final long NANO_2_MILLIS = 1000000L;


    /**
     * The current running state of the StopWatch.
     */
    private RunningState runningState = RunningState.STATE_UNSTARTED;

    /**
     * The start time.
     */
    private long startTime;

    private long timeAdding;

    /**
     * The start time in Millis - nanoTime is only for elapsed time so we
     * need to also store the currentTimeMillis to maintain the old
     * getStartTime API.
     */
    private long startTimeMillis;

    /**
     * The stop time.
     */
    private long stopTime;

    /**
     * <p>
     * Constructor.
     * </p>
     */
    public StopWatchImpl() {
        super();
    }

    /**
     * <p>
     * Start the stopwatch.
     * </p>
     *
     * <p>
     * This call starts a new timing session, clearing any previous values.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is already running.
     */
    public void start() {
        if (this.runningState == RunningState.STATE_STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        }
        if (this.runningState != RunningState.STATE_UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. ");
        }
        this.timeAdding=0;
        this.startTime = System.nanoTime();
        this.startTimeMillis = System.nanoTime()/NANO_2_MILLIS;
        this.runningState = RunningState.STATE_RUNNING;
    }

    /**
     * <p>
     * Start the stopwatch.
     * </p>
     *
     * @param startTime in nanoSeconds
     *                  <p>
     *                  This call starts a new timing session, clearing any previous values.
     *                  </p>
     * @throws IllegalStateException if the StopWatch is already running.
     */
    public void start(long startTime) {
        if (this.runningState == RunningState.STATE_STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        }
        if (this.runningState != RunningState.STATE_UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. ");
        }
        this.timeAdding=startTime;
        this.startTime = System.nanoTime();
        this.startTimeMillis = System.nanoTime() / NANO_2_MILLIS;
        this.runningState = RunningState.STATE_RUNNING;
    }


    /**
     * <p>
     * Stop the stopwatch.
     * </p>
     *
     * <p>
     * This call ends a new timing session, allowing the time to be retrieved.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void stop() {
        if (this.runningState != RunningState.STATE_RUNNING && this.runningState != RunningState.STATE_SUSPENDED) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        if (this.runningState == RunningState.STATE_RUNNING) {
            this.stopTime = System.nanoTime();
        }
        this.runningState = RunningState.STATE_STOPPED;
    }

    /**
     * <p>
     * Resets the stopwatch. Stops it if need be.
     * </p>
     *
     * <p>
     * This call clears the internal values to allow the object to be reused.
     * </p>
     */
    public void reset() {
        this.runningState = RunningState.STATE_UNSTARTED;
    }


    /**
     * <p>
     * Suspend the stopwatch for later resumption.
     * </p>
     *
     * <p>
     * This call suspends the watch until it is resumed. The watch will not include time between the suspend and
     * resume calls in the total time.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not currently running.
     */
    public void suspend() {
        if (this.runningState != RunningState.STATE_RUNNING) {
            throw new IllegalStateException("Stopwatch must be running to suspend. ");
        }
        this.stopTime = System.nanoTime();
        this.runningState = RunningState.STATE_SUSPENDED;
    }

    /**
     * <p>
     * Resume the stopwatch after a suspend.
     * </p>
     *
     * <p>
     * This call resumes the watch after it was suspended. The watch will not include time between the suspend and
     * resume calls in the total time.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch has not been suspended.
     */
    public void resume() {
        if (this.runningState != RunningState.STATE_SUSPENDED) {
            throw new IllegalStateException("Stopwatch must be suspended to resume. ");
        }
        this.startTime += (System.nanoTime() - this.stopTime);
        this.runningState = RunningState.STATE_RUNNING;
    }

    /**
     * <p>
     * Get the time on the stopwatch.
     * </p>
     *
     * <p>
     * This is either the time between the start and the moment this call is called, or the amount of time between
     * start and stop.
     * </p>
     *
     * @return the time in Nano
     */
    public long getTime() {
        return getNanoTime();
    }

    /**
     * <p>
     * Get the time on the stopwatch in nanoseconds.
     * </p>
     *
     * <p>
     * This is either the time between the start and the moment this call is called, or the amount of time between
     * start and stop.
     * </p>
     *
     * @return the time in nanoseconds
     * @since 3.0
     */
    public long getNanoTime() {
        if (this.runningState == RunningState.STATE_STOPPED || this.runningState == RunningState.STATE_SUSPENDED) {
            return (this.stopTime+timeAdding) - this.startTime;
        } else if (this.runningState == RunningState.STATE_UNSTARTED) {
            return 0;
        } else if (this.runningState == RunningState.STATE_RUNNING) {
            return (System.nanoTime()+timeAdding) - this.startTime;
        }
        throw new RuntimeException("Illegal running state has occured. ");
    }


    /**
     * Returns the time this stopwatch was started.
     *
     * @return the time this stopwatch was started
     * @throws IllegalStateException if this StopWatch has not been started
     * @since 2.4
     */
    public long getStartTime() {
        if (this.runningState == RunningState.STATE_UNSTARTED) {
            throw new IllegalStateException("Stopwatch has not been started");
        }
        // System.nanoTime is for elapsed time
        return this.startTimeMillis;
    }

    @Override
    public RunningState getCurrentState() {
        return runningState;
    }
}
