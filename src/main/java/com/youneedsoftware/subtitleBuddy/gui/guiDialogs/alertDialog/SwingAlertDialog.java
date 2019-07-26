package com.youneedsoftware.subtitleBuddy.gui.guiDialogs.alertDialog;

import com.google.inject.Singleton;

import javax.swing.*;

@Singleton
public class SwingAlertDialog implements AlertDialog {

    @Override
    public void tellUser(String message) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop( true );
        frame.setLocationByPlatform( true );
        frame.pack();
        frame.setVisible( true );
        JOptionPane.showMessageDialog(frame, message);
    }
}
