package edu.univ.erp.ui;

import edu.univ.erp.access.AccessChecker;
import edu.univ.erp.service.AdminService;

import javax.swing.*;
import java.awt.*;

public class AdminMaintenancePanel extends JPanel {

    private final AdminService adminService;
    private final AccessChecker accessChecker;

    private JLabel statusLabel;
    private JButton toggleButton;

    private JTextField deadlineField;

    public AdminMaintenancePanel() {
        this.adminService = new AdminService();
        this.accessChecker = new AccessChecker();

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Maintenance & Settings"); // Updated Title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        add(titleLabel, c);

        c.gridx = 0; c.gridy = 1; c.gridwidth = 1;
        add(new JLabel("Maintenance Status:"), c);

        statusLabel = new JLabel();
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        c.gridx = 1; c.gridy = 1;
        add(statusLabel, c);

        toggleButton = new JButton();
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        toggleButton.addActionListener(e -> onToggle());
        add(toggleButton, c);

        // --- Divider ---
        c.gridx = 0; c.gridy = 3;
        add(new JSeparator(), c);

        // --- 2. Registration Deadline Section ---
        c.gridy = 4;
        JLabel deadlineLabel = new JLabel("Registration Deadline (YYYY-MM-DD):");
        deadlineLabel.setFont(deadlineLabel.getFont().deriveFont(Font.BOLD));
        add(deadlineLabel, c);

        JPanel deadlinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deadlineField = new JTextField(15);
        JButton updateDeadlineButton = new JButton("Update");
        updateDeadlineButton.addActionListener(e -> onUpdateDeadline());

        deadlinePanel.add(deadlineField);
        deadlinePanel.add(updateDeadlineButton);

        c.gridy = 5;
        add(deadlinePanel, c);

        // Initial State
        updateStatus();
        loadDeadline(); // Load the date
    }

    /**
     * Updates the UI to reflect the current maintenance status.
     * Public so MainDashboard can refresh it.
     */
    public void updateStatus() {
        boolean isCurrentlyOn = accessChecker.isMaintenanceOn();
        if (isCurrentlyOn) {
            statusLabel.setText("ON");
            statusLabel.setForeground(Color.RED);
            toggleButton.setText("Turn Maintenance Mode OFF");
            toggleButton.setBackground(new Color(25, 135, 84));
            toggleButton.setForeground(Color.WHITE);
        } else {
            statusLabel.setText("OFF");
            statusLabel.setForeground(new Color(25, 135, 84));
            toggleButton.setText("Turn Maintenance Mode ON");
            toggleButton.setBackground(new Color(220, 53, 69));
            toggleButton.setForeground(Color.WHITE);
        }
    }

    private void onToggle() {
        boolean isCurrentlyOn = accessChecker.isMaintenanceOn();
        String message = adminService.setMaintenanceMode(!isCurrentlyOn);
        JOptionPane.showMessageDialog(this, message);
        updateStatus();
    }

    // --- New Methods for Deadline ---

    private void loadDeadline() {
        String current = adminService.getRegistrationDeadline();
        deadlineField.setText(current);
    }

    private void onUpdateDeadline() {
        String newDeadline = deadlineField.getText().trim();
        String message = adminService.updateRegistrationDeadline(newDeadline);
        JOptionPane.showMessageDialog(this, message);
        loadDeadline();
    }
}