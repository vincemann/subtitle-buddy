package io.github.vincemann.subtitleBuddy.gui.dialog.continueDialog;

import com.google.inject.Singleton;

import javax.swing.*;

@Singleton
public class SwingContinueDialog implements ContinueDialog {

    @Override
    public boolean askUserToContinue(String message) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        //todo externalize string
        int dialogResult = JOptionPane.showConfirmDialog (null, message,"Warning",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            return true;
        }else {
            return false;
        }
    }
}
