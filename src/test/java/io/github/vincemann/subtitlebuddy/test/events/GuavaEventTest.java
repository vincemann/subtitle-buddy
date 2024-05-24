package io.github.vincemann.subtitlebuddy.test.events;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.github.vincemann.subtitlebuddy.events.DoneParsingEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuavaEventTest {

    private boolean arrived;
    private boolean genericEvent;
    private EventBus eventBus;

    class TestListener {

        @Subscribe
        public void getEvent(DoneParsingEvent e){
            arrived=true;
        }

        @Subscribe public void getGenericEvent(Object e){
            genericEvent=true;
        }

        @Subscribe
        public void testCascadeEvent(TestEvent e){
            Assert.assertFalse(arrived);
            eventBus.post(new DoneParsingEvent());
            Assert.assertFalse(arrived);
        }
    }

    private class TestEvent{ }
    @Before
    public void init(){
        this.eventBus = new EventBus();
        eventBus.register(new TestListener());
    }

    @Test
    public void testAquireEvent(){
        Assert.assertFalse(arrived);
        eventBus.post(new DoneParsingEvent());
        Assert.assertTrue(arrived);
    }

    @Test
    public void testGenericEvent(){
        Assert.assertFalse(genericEvent);
        eventBus.post(new DoneParsingEvent());
        Assert.assertTrue(genericEvent);
    }


    @Test
    public void testEventCascade(){
        eventBus.post(new TestEvent());
        Assert.assertTrue(arrived);
    }

    @After
    public void cleanUp(){
        this.arrived=false;
        genericEvent=false;
    }


}
