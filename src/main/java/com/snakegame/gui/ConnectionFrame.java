
package com.snakegame.gui;

import javax.swing.*;
import java.awt.*;

public class ConnectionFrame extends JFrame {
    private final JTextField hostField;
    private final JTextField portField;
    private final JButton connectButton;

    public ConnectionFrame() {
        setTitle("Connect to Server");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Host IP:"));
        hostField = new JTextField("localhost");
        add(hostField);

        add(new JLabel("Port:"));
        portField = new JTextField("8080");
        add(portField);

        add(new JLabel()); // Empty cell
        connectButton = new JButton("Connect");
        add(connectButton);

        connectButton.addActionListener(e -> connect());
    }

    private void connect() {
        String host = hostField.getText();
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setVisible(false);

        GameFrame gameFrame = new GameFrame();
        gameFrame.setVisible(true);
        gameFrame.connectToServer(host, port);

        dispose();
    }
}
