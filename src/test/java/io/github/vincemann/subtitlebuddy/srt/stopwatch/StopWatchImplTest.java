package io.github.vincemann.subtitlebuddy.srt.stopwatch;

import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StopWatchImplTest {
    private static final long NANO_2_MILLIS = 1000000L;

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        this.stopWatch= new StopWatchImpl();
    }

    @Test
    public void testStopWatchToTimeStamp(){
        //todo might fail if pc is too slow
        long fiveMinutesInMillis = 300000L;
        stopWatch.start(fiveMinutesInMillis*NANO_2_MILLIS);
        stopWatch.suspend();


        Timestamp timestamp = new Timestamp(stopWatch.getTime()/NANO_2_MILLIS);
        Assert.assertEquals(0,timestamp.getHours());
        Assert.assertEquals(5,timestamp.getMinutes());
        Assert.assertEquals(0,timestamp.getSeconds());
    }

    @Test
    public void testTimeStampToStopWatch(){
        //todo might fail if pc is too slow
        Timestamp timestamp = new Timestamp(0,5,0,0);
        stopWatch.start(timestamp.toMilliSeconds()*NANO_2_MILLIS);
        stopWatch.suspend();

        Assert.assertEquals(timestamp.toMilliSeconds(),stopWatch.getTime()/NANO_2_MILLIS);
    }
}
