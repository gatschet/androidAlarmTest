
package ch.dvbern.alarmtest;

public enum NotificationType {
    BAD_SYMPTOM_DETECTED(1, 30), BAD_SYMPTOM_PUBLISHED(2, 40), BAD_SYMPTOM_CONFIRMED(3, 50), BAD_SYMPTOM_MISSION_REQUESTED(
            4, 20), BUDDY_AID_MISSION_REQUESTED(7, 21), BUDDY_AID_CONFIRMED(8, 51), MISSION_CANCELED(5, 10), SAFETY_CHECK(9, 25), NONE(6, 0);

    private final int value;
    private final int priority; // overwriting rules when notifying. higher number = higher priority

    NotificationType(final int value, final int priority) {
        this.value = value;
        this.priority = priority;
    }

    public int getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }

    public static NotificationType valueOf(final int value) {
        for (NotificationType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }

        return NotificationType.NONE; // failsafe impl
    }

}
