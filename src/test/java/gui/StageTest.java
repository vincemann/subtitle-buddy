package gui;

import com.youneedsoftware.subtitleBuddy.gui.srtDisplayer.SrtDisplayer;
import com.youneedsoftware.subtitleBuddy.gui.stages.StageState;
import com.youneedsoftware.subtitleBuddy.gui.stages.stageController.AbstractStageController;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeoutException;

public abstract class StageTest extends GuiTest {


    protected boolean isStageShowing(Class<? extends Annotation> a){
        AbstractStageController abstractStageController = getInstance(AbstractStageController.class,a);
        return abstractStageController.getStageState().equals(StageState.OPEN);
    }

    /**
     * holt stage to front und returned erst wenn stage @ front ist
     * @param a
     */
    protected void focusStage(Class<? extends Annotation> a) throws TimeoutException {
        AbstractStageController abstractStageController = getInstance(AbstractStageController.class,a);
        Runnable toFrontTask = () -> {
            abstractStageController.getStage().toFront();
            abstractStageController.getStage().requestFocus();
        };
        doOnFxThreadAndWait(toFrontTask);
        refreshGui();
        if(!abstractStageController.getStage().isFocused()){
            throw new IllegalStateException("Stage is not focused");
        }
    }

    protected AbstractStageController  findStageController(Class<? extends Annotation> stageAnnotation) {
        return getInstance(AbstractStageController.class, stageAnnotation);
    }

    protected SrtDisplayer findSrtDisplayer(Class<? extends SrtDisplayer> modeAnnotation){
        return getInstance(modeAnnotation);
    }

}
