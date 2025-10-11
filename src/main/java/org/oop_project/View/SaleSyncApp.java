package org.oop_project.View;

import javax.swing .*;
import java.awt .*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SaleSyncApp {

    // Label whose text will be updated by the button click
    private final JLabel statusLabel;
    // Counter for button clicks
    private int clickCount = 0;

    /**
     * Constructor sets up the entire GUI.
     */
    public SaleSyncApp() {
        // 1. Create the main window (JFrame)
        // Main frame of the application
        JFrame frame = new JFrame("Simple Swing Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // 2. Create components

        // The label will show the current status
        statusLabel = new JLabel("Click the button to start!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // The button that triggers the action
        JButton clickButton = new JButton("Click Me!");
        clickButton.setBackground(new Color(60, 140, 250)); // Light blue background
        clickButton.setForeground(Color.WHITE);             // White text
        clickButton.setFocusPainted(false);                 // Remove focus border
        clickButton.setFont(new Font("Arial", Font.BOLD, 14));

        // 3. Add an ActionListener to the button
        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code runs when the button is pressed
                clickCount++;
                statusLabel.setText("Button has been clicked " + clickCount + " time(s).");
            }
        });

        // 4. Arrange components using a Layout Manager (BorderLayout in this case)

        // Use a JPanel to hold and slightly pad the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(clickButton);

        // Add components to the frame
        frame.add(statusLabel, BorderLayout.CENTER); // Label in the middle
        frame.add(buttonPanel, BorderLayout.SOUTH); // Button panel at the bottom

        // 5. Make the window visible
        frame.setVisible(true);
    }

}
