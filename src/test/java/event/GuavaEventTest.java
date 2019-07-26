package event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.youneedsoftware.subtitleBuddy.events.DoneParsingEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuavaEventTest {

    private boolean arrived;
    private boolean genericEvent;
    private EventBus eventBus;

    class TestListener {

        @Subscribe public void getEvent(DoneParsingEvent e){
            arrived=true;
        }

        @Subscribe public void getGenericEvent(Object e){
            genericEvent=true;
        }

        @Subscribe public void testCascadeEvent(TestEvent e){
            Assert.assertFalse(arrived);
            eventBus.post(new DoneParsingEvent());
            //doneparsingevent wird erst behandelt werden wenn wir durch sind
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
        //folgeEvent wurde aber letztlich doch ausgeführt
        Assert.assertTrue(arrived);
    }

    @After
    public void cleanUp(){
        this.arrived=false;
        genericEvent=false;
    }


}
