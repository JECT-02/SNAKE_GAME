
package com.snakegame.main;

import com.snakegame.gui.ConnectionFrame;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConnectionFrame connectionFrame = new ConnectionFrame();
            connectionFrame.setVisible(true);
        });
    }
}
