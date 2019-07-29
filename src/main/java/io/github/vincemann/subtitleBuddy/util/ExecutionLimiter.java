package io.github.vincemann.subtitleBuddy.util;

import io.github.vincemann.subtitleBuddy.util.FxUtils.VoidMethod;


public class ExecutionLimiter {

    private long lastTimeRun;
    private long delta;
    private VoidMethod voidMethod;

    /**
     * only executes the voidMethod function when the last time called was min delta nano ago
     * @param delta         in nano
     * @param voidMethod
     */
    public ExecutionLimiter(long delta, VoidMethod voidMethod) {
        this.delta = delta;
        this.voidMethod=voidMethod;
        this.lastTimeRun=0;
    }

    public boolean tryExecuting(){
        long curr = System.nanoTime();
        if(curr-lastTimeRun>=delta){
            lastTimeRun=curr;
            voidMethod.call();
            return true;
        }
        return false;
    }
}
