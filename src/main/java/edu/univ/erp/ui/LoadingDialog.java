package edu.univ.erp.ui;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog {

    public LoadingDialog(Window owner, String message) {
        super(owner, "Please Wait", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Prevent closing by user
        setSize(300, 100);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(15, 15));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Message Label
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD));
        panel.add(messageLabel, BorderLayout.NORTH);

        // Progress Bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Makes it spin back and forth
        panel.add(progressBar, BorderLayout.CENTER);

        add(panel);
    }
}