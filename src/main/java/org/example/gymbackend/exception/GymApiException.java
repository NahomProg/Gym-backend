package org.example.gymbackend.exception;

import org.springframework.http.HttpStatus;

public class GymApiException extends RuntimeException {

    private final HttpStatus status;

    public GymApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static GymApiException emailAlreadyExists(String email) {
        return new GymApiException("Email already registered: " + email, HttpStatus.CONFLICT);
    }

    public static GymApiException roleNotFound(String roleName) {
        return new GymApiException("Role not found: " + roleName, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static GymApiException userNotFound(String email) {
        return new GymApiException("User not found: " + email, HttpStatus.NOT_FOUND);
    }

    public static GymApiException memberNotFound(String id) {
        return new GymApiException("Member not found: " + id, HttpStatus.NOT_FOUND);
    }

    public static GymApiException memberEmailAlreadyExists(String email) {
        return new GymApiException("A member with this email already exists: " + email, HttpStatus.CONFLICT);
    }

    public static GymApiException memberPhoneAlreadyExists(String phoneNumber) {
        return new GymApiException("A member with this phone number already exists: " + phoneNumber, HttpStatus.CONFLICT);
    }

    public static GymApiException memberNotActive(String id) {
        return new GymApiException("Member is not active (suspended or inactive members cannot be assigned a new membership): " + id, HttpStatus.CONFLICT);
    }

    public static GymApiException membershipPlanNotFound(String id) {
        return new GymApiException("Membership plan not found: " + id, HttpStatus.NOT_FOUND);
    }

    public static GymApiException membershipPlanAlreadyExists(String name) {
        return new GymApiException("A membership plan with this name already exists: " + name, HttpStatus.CONFLICT);
    }

    public static GymApiException membershipNotFound(String id) {
        return new GymApiException("Membership not found: " + id, HttpStatus.NOT_FOUND);
    }

    public static GymApiException equipmentNotFound(String id) {
        return new GymApiException("Equipment not found: " + id, HttpStatus.NOT_FOUND);
    }

    public static GymApiException equipmentAlreadyUnderMaintenance(String id) {
        return new GymApiException("Equipment is already under maintenance: " + id, HttpStatus.CONFLICT);
    }

    public static GymApiException maintenanceRecordNotFound(String recordId) {
        return new GymApiException("Maintenance record not found: " + recordId, HttpStatus.NOT_FOUND);
    }

    public static GymApiException maintenanceAlreadyResolved(String recordId) {
        return new GymApiException("Maintenance record is already resolved: " + recordId, HttpStatus.CONFLICT);
    }

    public static GymApiException statusChangeRequiresMaintenanceWorkflow(String id) {
        return new GymApiException(
                "Use the maintenance start/resolve endpoints to move equipment " + id + " into or out of UNDER_MAINTENANCE",
                HttpStatus.CONFLICT);
    }

    public static GymApiException classSessionNotFound(String id) {
        return new GymApiException("Class session not found: " + id, HttpStatus.NOT_FOUND);
    }

    public static GymApiException alreadyEnrolled(String memberId, String classSessionId) {
        return new GymApiException("Member " + memberId + " is already enrolled in class " + classSessionId, HttpStatus.CONFLICT);
    }

    public static GymApiException enrollmentNotFound(String memberId, String classSessionId) {
        return new GymApiException("No active enrollment found for member " + memberId + " in class " + classSessionId, HttpStatus.NOT_FOUND);
    }

    public static GymApiException noActiveMembership(String memberId) {
        return new GymApiException("Member does not have an active membership: " + memberId, HttpStatus.CONFLICT);
    }

    public static GymApiException alreadyCheckedIn(String memberId) {
        return new GymApiException("Member is already checked in: " + memberId, HttpStatus.CONFLICT);
    }

    public static GymApiException noActiveCheckIn(String memberId) {
        return new GymApiException("Member has no active check-in to check out from: " + memberId, HttpStatus.BAD_REQUEST);
    }
}