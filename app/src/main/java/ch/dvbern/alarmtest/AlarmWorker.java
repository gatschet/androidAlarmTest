package ch.dvbern.alarmtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.NOTIFICATION_SERVICE;
import static ch.dvbern.alarmtest.NotificationType.BAD_SYMPTOM_DETECTED;

public class AlarmWorker extends Worker {

    private static final int FOREGROUND_SERVICE_ID = 111;

    private Context context;


    private NotificationManager notificationManager;

    public AlarmWorker(
            @NonNull Context context,
            @NonNull WorkerParameters parameters) {
        super(context, parameters);
        this.context = context;
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();

        // Mark the Worker as important
        String progress = "Starting Alarm";
        setForegroundAsync(createForegroundInfo(progress));

        Intent notifyIntent = NotificationService.buildShowNotificationIntent(context, BAD_SYMPTOM_DETECTED, true);
        NotificationService.enqueueWork(context, notifyIntent);

        return Result.success();
    }

    @NonNull
    private ForegroundInfo createForegroundInfo(@NonNull String progress) {
        // Build a notification using bytesRead and contentLength

        Context context = getApplicationContext();
        //String id = context.getString(R.string.safety_notification_channel_name);
        String id = "other notifications";
        String title = context.getString(R.string.notification_title);
        String cancel = context.getString(R.string.cancel_download);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id);
        }

        Notification notification = new NotificationCompat.Builder(context, id)
                .setContentTitle(title)
                .setTicker(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                .addAction(android.R.drawable.ic_delete, cancel, intent)
                .build();

        return new ForegroundInfo(FOREGROUND_SERVICE_ID, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(String id) {
        String channelName = context.getString(R.string.safety_notification_channel_name);
        //String description = getString(R.string.channel_description);
        String description = "XXXXX";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        android.app.NotificationChannel channel = new android.app.NotificationChannel(id, channelName, importance);
        channel.setDescription(description);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(channel);
    }

}
