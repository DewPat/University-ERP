package edu.univ.erp.service;

import edu.univ.erp.auth.AuthService;
import edu.univ.erp.data.AuthDataStore;
import edu.univ.erp.data.ErpDataStore;
import edu.univ.erp.domain.LockedUser;
import edu.univ.erp.domain.UserRow; // Make sure this is imported

import java.util.List;

public class AdminService {

    private final ErpDataStore erpDataStore;
    private final AuthService authService;

    // --- THIS WAS MISSING ---
    private final AuthDataStore authDataStore;
    // ------------------------

    public AdminService() {
        this.erpDataStore = new ErpDataStore();
        this.authService = new AuthService();
        // Initialize the authDataStore here
        this.authDataStore = new AuthDataStore();
    }

    // --- Course & Section Management ---

    public String createCourse(String code, String title, String creditsStr) {
        if (code.isEmpty() || title.isEmpty() || creditsStr.isEmpty()) return "Error: All fields required.";
        try {
            int credits = Integer.parseInt(creditsStr);
            if (credits <= 0) throw new NumberFormatException();
            boolean success = erpDataStore.createCourse(code, title, credits);
            return success ? "Success! Course created." : "Error: Could not create course.";
        } catch (NumberFormatException e) { return "Error: Invalid credits."; }
    }

    public String createSection(int courseId, Integer instructorId, String dayTime, String room,
                                String capacityStr, String semester, String yearStr) {
        if (dayTime.isEmpty() || room.isEmpty() || capacityStr.isEmpty()) return "Error: All fields required.";
        try {
            int capacity = Integer.parseInt(capacityStr);
            int year = Integer.parseInt(yearStr);
            if (capacity < 0) {
                return "Error: Capacity cannot be negative.";
            }
            boolean success = erpDataStore.createSection(courseId, instructorId, dayTime, room, capacity, semester, year);
            return success ? "Success! Section created." : "Error: Could not create section.";
        } catch (NumberFormatException e) { return "Error: Invalid numbers."; }
    }

    public String updateSection(int sectionId, Integer instructorId, String dayTime, String room,
                                String capacityStr, String semester, String yearStr) {
        try {
            int capacity = Integer.parseInt(capacityStr);
            int year = Integer.parseInt(yearStr);
            if (capacity < 0) {
                return "Error: Capacity cannot be negative.";
            }
            boolean success = erpDataStore.updateSection(sectionId, instructorId, dayTime, room, capacity, semester, year);
            return success ? "Success! Section updated." : "Error: Update failed.";
        } catch (Exception e) { return "Error: Invalid input."; }
    }

    public String deleteSection(int sectionId) {
        if (erpDataStore.hasEnrollments(sectionId)) return "Error: Cannot delete. Students are enrolled.";
        boolean success = erpDataStore.deleteSection(sectionId);
        return success ? "Success! Section deleted." : "Error: Delete failed.";
    }

    public String deleteCourse(int courseId) {
        // Assuming CASCADE delete is enabled or no sections exist
        boolean success = erpDataStore.deleteCourse(courseId);
        return success ? "Success! Course deleted." : "Error: Delete failed (Check if sections exist).";
    }

    // --- User Management ---

    public String createNewUser(String username, String password, String role,
                                String rollNo, String program, String yearStr,
                                String department, String title) {
        if (username.isEmpty() || password.isEmpty()) return "Error: Credentials required.";

        int newUserId = authService.createUser(username, password, role);
        if (newUserId == -1) return "Error: Username taken.";

        boolean profileSuccess = false;
        try {
            if ("Student".equals(role)) {
                profileSuccess = erpDataStore.createStudentProfile(newUserId, rollNo, program, Integer.parseInt(yearStr));
            } else if ("Instructor".equals(role)) {
                profileSuccess = erpDataStore.createInstructorProfile(newUserId, department, title);
            } else {
                profileSuccess = true; // Admin
            }
        } catch (Exception e) { return "Error: Invalid profile data."; }

        return profileSuccess ? "Success! User created." : "Error: Profile creation failed.";
    }

    public List<UserRow> getAllUsers() {
        return authDataStore.getAllUsers();
    }

    // --- Maintenance & Security ---

    public String setMaintenanceMode(boolean isEnabled) {
        return erpDataStore.updateSetting("maintenance_on", isEnabled ? "true" : "false")
                ? "Success! Maintenance is " + (isEnabled ? "ON" : "OFF")
                : "Error updating setting.";
    }

    public String updateRegistrationDeadline(String deadlineStr) {
        try {
            java.time.LocalDate.parse(deadlineStr); // Validate format
        } catch (java.time.format.DateTimeParseException e) {
            return "Error: Invalid date format. Please use YYYY-MM-DD.";
        }
        boolean success = erpDataStore.updateSetting("registration_deadline", deadlineStr);
        return success ? "Success! Registration deadline updated." : "Error: Could not update deadline.";
    }

    public String getRegistrationDeadline() {
        String deadline = erpDataStore.getSetting("registration_deadline");
        return (deadline != null) ? deadline : "Not Set";
    }

    public List<LockedUser> getLockedUsers() {
        // Now authDataStore is recognized
        return authDataStore.getUsersWithFailedAttempts();
    }

    public String unlockUser(String username) {
        // Now authDataStore is recognized
        authDataStore.resetFailedAttempts(username);
        return "Success! User unlocked.";
    }

    public String adminResetPassword(String username, String newPassword) {
        if (newPassword == null || newPassword.length() < 8) return "Error: Password too short.";
        return authService.resetPasswordForUser(username, newPassword)
                ? "Success! Password reset."
                : "Error resetting password.";
    }
}