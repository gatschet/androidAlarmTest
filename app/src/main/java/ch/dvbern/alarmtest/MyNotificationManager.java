
package ch.dvbern.alarmtest;

import android.content.Context;
import android.content.Intent;

public final class MyNotificationManager {

    /**
     * Higher notification importance: shows everywhere, makes noise and peeks. May use full screen
     * intents.
     */
    public static final String ANDROID_NOTIFICATION_CHANNEL_ID = "uepaa-safety-app-alerts";

    private MyNotificationManager() {

    }

    /**
     * Notifies wakefully with a notification
     */
    public static void notify(final Context context, final NotificationType notificationType) {
        sendNotifyIntent(context, notificationType, false);
    }

    /**
     * Notifies wakefully with an alarming notification
     */
    public static void notifyAlarm(final Context context, final NotificationType notificationType) {
        sendNotifyIntent(context, notificationType, true);
    }

    private static void sendNotifyIntent(final Context context, final NotificationType notificationType, final boolean isAlarmNotification) {
        Intent notifyIntent = NotificationService.buildShowNotificationIntent(context, notificationType, isAlarmNotification);
        NotificationService.enqueueWork(context, notifyIntent);
    }

}
