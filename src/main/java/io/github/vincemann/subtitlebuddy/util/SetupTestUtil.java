package io.github.vincemann.subtitlebuddy.util;

import java.util.concurrent.Callable;

public class SetupTestUtil {

    /**
     * Wait until application is set up
     * Then exit with 0 code.
     * @param readyCondition
     */
    public static void performSetupTest(Callable<Boolean> readyCondition) {
        new Thread(() -> {
            try {
                while (!readyCondition.call()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
