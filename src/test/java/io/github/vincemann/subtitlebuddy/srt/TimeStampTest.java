package io.github.vincemann.subtitlebuddy.srt;

import org.junit.Assert;
import org.junit.Test;


public class TimeStampTest {

    @Test
    public void testAdjustTimeStamp(){
        Timestamp timestamp = new Timestamp(0,0,300,0);
        Assert.assertEquals(5,timestamp.getMinutes());
        Assert.assertEquals(0,timestamp.getSeconds());

        Timestamp timestamp2 = new Timestamp(0,0,400,5000);
        Assert.assertEquals(6,timestamp2.getMinutes());
        Assert.assertEquals(45,timestamp2.getSeconds());
    }

    @Test
    public void testComparing(){
        Timestamp timestamp = new Timestamp(1,20,12,0);
        Timestamp secondTimeStamp = new Timestamp(1,20,13,0);
        Timestamp thirdTimeStamp = new Timestamp(1,20,12,300);

        assert timestamp.compareTo(secondTimeStamp) < 0;
        assert timestamp.compareTo(thirdTimeStamp) < 0 ;
        assert thirdTimeStamp.compareTo(secondTimeStamp) <0;
        assert thirdTimeStamp.compareTo(timestamp) > 0;
    }

}
