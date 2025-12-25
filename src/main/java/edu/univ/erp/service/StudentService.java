package edu.univ.erp.service;

import edu.univ.erp.access.AccessChecker;
import edu.univ.erp.data.ErpDataStore;
import edu.univ.erp.domain.Section;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class StudentService {

    private final ErpDataStore erpDataStore;
    private final AccessChecker accessChecker;

    public StudentService() {
        this.erpDataStore = new ErpDataStore();
        this.accessChecker = new AccessChecker();
    }

    /**
     * Helper method to check if the registration/drop deadline has passed.
     * @return An error message string if the deadline has passed, or null if it is okay to proceed.
     */
    private String checkDeadline() {
        String deadlineStr = erpDataStore.getSetting("registration_deadline");
        if (deadlineStr != null) {
            try {
                LocalDate deadline = LocalDate.parse(deadlineStr); // Expects format YYYY-MM-DD
                if (LocalDate.now().isAfter(deadline)) {
                    return "Error: The registration/drop deadline has passed (" + deadlineStr + ").";
                }
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format in settings table for 'registration_deadline': " + deadlineStr);
            }
        }
        return null; // No error, proceed
    }

    /**
     * Attempts to register a student for a section after checking all rules.
     * @return A clear success or error message for the UI.
     */
    public String registerForSection(int studentId, int sectionId) {
        if (accessChecker.isMaintenanceOn()) {
            return "Maintenance Mode is ON. Registration is disabled.";
        }
        String deadlineError = checkDeadline();
        if (deadlineError != null) {
            return deadlineError;
        }
        if (erpDataStore.isStudentAlreadyEnrolled(studentId, sectionId)) {
            return "Error: You are already registered for this section.";
        }
        Section section = erpDataStore.getSectionDetails(sectionId);
        if (section == null) {
            return "Error: Section not found.";
        }
        if (section.isFull()) {
            return "Error: Section full.";
        }
        boolean success = erpDataStore.createEnrollment(studentId, sectionId);
        if (success) {
            return "Success! You are now registered.";
        } else {
            return "Error: Registration failed for an unknown reason.";
        }
    }

    /**
     * Attempts to drop a section after checking all rules.
     * @return A clear success or error message for the UI.
     */
    public String dropSection(int enrollmentId) {
        if (accessChecker.isMaintenanceOn()) {
            return "Maintenance Mode is ON. Dropping sections is disabled.";
        }
        String deadlineError = checkDeadline();
        if (deadlineError != null) {
            return deadlineError;
        }
        if (erpDataStore.hasGrades(enrollmentId)) {
            return "Error: Cannot drop a course after grades have been entered.";
        }
        boolean success = erpDataStore.deleteEnrollment(enrollmentId);
        if (success) {
            return "Success! Section has been dropped.";
        } else {
            return "Error: Dropping the section failed.";
        }
    }
}