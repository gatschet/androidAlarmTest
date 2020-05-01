
package ch.dvbern.alarmtest;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class NotificationService extends JobIntentService {

    static final String INTENT_ACTION_SHOW_NOTIFICATION = "INTENT_ACTION_SHOW_NOTIFICATION";
    static final String INTENT_EXTRA_NOTIFICATION_TYPE = "INTENT_EXTRA_NOTIFICATION_TYPE";
    static final String INTENT_EXTRA_NOTIFICATION_TYPE_ALARM = "INTENT_EXTRA_NOTIFICATION_TYPE_ALARM";
    static final String INTENT_ACTION_CLOSE_LOCK_SCREEN_NOTIFICATION = "INTENT_ACTION_CLOSE_LOCK_SCREEN_NOTIFICATION";

    public static final String BROADCAST_NOTIFICATION_PENDING = "BROADCAST_NOTIFICATION_PENDING";

    private static final String TAG = NotificationService.class.getSimpleName();

    public NotificationService() {
        super();
    }

    private static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, NotificationService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        Log.i(TAG, "doWakefulWork | action = " + action);

        if (INTENT_ACTION_SHOW_NOTIFICATION.equals(action)) {

            NotificationType notificationType = NotificationType.valueOf(intent.getExtras().getInt(INTENT_EXTRA_NOTIFICATION_TYPE));
            boolean alarmingNotification = intent.getBooleanExtra(INTENT_EXTRA_NOTIFICATION_TYPE_ALARM, false);

            showPendingNotification(this, intent, notificationType, alarmingNotification);

        } else if (INTENT_ACTION_CLOSE_LOCK_SCREEN_NOTIFICATION.equals(action)) {
            dismissPopupOnLockScreen(this);
        }
    }

    public static Intent buildShowNotificationIntent(final Context context, final NotificationType notificationType,
                                                     final boolean isAlarmNotification) {
        final Intent intent = new Intent();
        intent.setAction(INTENT_ACTION_SHOW_NOTIFICATION);
        intent.putExtra(INTENT_EXTRA_NOTIFICATION_TYPE, notificationType.getValue());
        intent.putExtra(INTENT_EXTRA_NOTIFICATION_TYPE_ALARM, isAlarmNotification);
        return intent.setClass(context, NotificationService.class);
    }

    private void showPendingNotification(final Context context, final Intent intent, NotificationType notificationType, final boolean alarmingNotification) {
        Log.i(TAG, "showPendingNotification | notificationType = " + notificationType.name() + " (alarmingNotification = " + alarmingNotification + ")");
        Log.i(TAG, "------------------------------------------->ch.dvbern.alarmtest : Here should display wakeup<-------------------------------------------");

        //showAlarmOnLockScreenV1(this, intent, alarmingNotification); // Version to test one
        showAlarmOnLockScreenVarImportanceHigh(this, intent, alarmingNotification); // Version to test with version two

    }


    private void showAlarmOnLockScreenV1(final Context context, final Intent intent, final boolean alarmingNotification) {
        Log.d(TAG, "showPopupOnLockScreen");

        Intent notificationStarterIntent = AlarmScreenActivity.buildNotificationStarterIntent(context, intent, alarmingNotification);
        startActivity(notificationStarterIntent);
    }


    private void showAlarmOnLockScreenVarImportanceHigh(final Context context, final Intent intent, final boolean alarmingNotification) {
        Log.d(TAG, "showPopupOnLockScreenVarImportanceHigh");

        Intent notificationStarterIntent = AlarmScreenActivity.buildNotificationStarterIntent(context, intent, alarmingNotification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d(TAG, "showPopupOnLockScreen XXX");


            String channelName = getResources().getString(R.string.safety_notification_channel_name);
            //String description = getString(R.string.channel_description);
            String description = "XXXXX";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            android.app.NotificationChannel channel = new android.app.NotificationChannel(MyNotificationManager.ANDROID_NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


//            Intent newintent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//            newintent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            notificationStarterIntent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());

        }

        startActivity(notificationStarterIntent);


    }

/*
    private void showPopupOnLockScreenXXXX(final Context context, final Intent intent, final boolean alarmingNotification) {
        Log.d(TAG, "showPopupOnLockScreenXXXXX");


        //startActivity(notificationStarterIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d(TAG, "showPopupOnLockScreen XXX");


            String channelName = getResources().getString(R.string.safety_notification_channel_name);
            //String description = getString(R.string.channel_description);
            String description = "XXXXX";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            android.app.NotificationChannel channel = new android.app.NotificationChannel(UepaaNetAndroidConstants.ANDROID_NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel.getId())
                    .setSmallIcon(R.drawable.notification_symptom_small)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            notificationManager.notify(123, builder.build());

        }

    }
*/

    private void dismissPopupOnLockScreen(final Context context) {
        Log.d(TAG, "dismissPopupOnLockScreen");

        AlarmScreenActivity.discardNotification(context);
    }

}
