package io.github.vincemann.subtitlebuddy.util;


import java.util.HashMap;
import java.util.Map;

public class ExecutionLimiter {

    private static final Map<String,ExecutionLimiter> registry = new HashMap<>();
    private long lastTimeRun;
    private long delta;
    private Runnable runnable;

    public static void executeMaxEveryNMillis(String key, long n, Runnable runnable){
        ExecutionLimiter limiter = registry.get(key);
        if (limiter == null){
            ExecutionLimiter el = new ExecutionLimiter(n, runnable);
            el.tryExecuting();
            registry.put(key,el);
            return;
        }
        limiter.tryExecuting();
    }

    /**
     * only executes the function when the last time called was min delta nano ago
     * @param delta         in nano
     * @param runnable
     */
    private ExecutionLimiter(long delta, Runnable runnable) {
        this.delta = delta;
        this.runnable =runnable;
        this.lastTimeRun=0;
    }

    private boolean tryExecuting(){
        long curr = System.nanoTime();
        if(curr-lastTimeRun>=delta){
            lastTimeRun=curr;
            runnable.run();
            return true;
        }
        return false;
    }
}
