package io.github.vincemann.subtitlebuddy.util;


public class ExecutionLimiter {

    private long lastTimeRun;
    private long delta;
    private Runnable runnable;

    /**
     * only executes the voidMethod function when the last time called was min delta nano ago
     * @param delta         in nano
     * @param runnable
     */
    public ExecutionLimiter(long delta, Runnable runnable) {
        this.delta = delta;
        this.runnable =runnable;
        this.lastTimeRun=0;
    }

    public boolean tryExecuting(){
        long curr = System.nanoTime();
        if(curr-lastTimeRun>=delta){
            lastTimeRun=curr;
            runnable.run();
            return true;
        }
        return false;
    }
}
