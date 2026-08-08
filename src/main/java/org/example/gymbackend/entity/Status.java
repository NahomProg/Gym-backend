package org.example.gymbackend.entity;

public class Status {

    public enum ClassStatus {
        SCHEDULED, IN_PROGRESS, FINISHED, CANCELLED
    }

    public enum MemberStatus {
        ACTIVE, SUSPENDED, INACTIVE
    }

    public enum MembershipStatus {
        ACTIVE, EXPIRED, SUSPENDED, CANCELLED
    }

    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, CANCELLED
    }

    public enum EquipmentStatus {
        AVAILABLE, IN_USE, UNDER_MAINTENANCE
    }

}
