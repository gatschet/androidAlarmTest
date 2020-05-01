package ch.dvbern.alarmtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends Service {

    private final IBinder serviceBinder = new MyServiceBinder();

    private static final long FIVE_SECONDS = 5 * 1000;

    public static final String PREFIX = MyService.class.getName();
    public static final String INTENT_ACTION_WAKE_UP = "ch.uepaa.alpinesafety.android.premium.symptomcheck.action_wakeUp";
    public static final String INTENT_ACTION_TRIGGER_SYMPTOM_CHECKER = PREFIX + ".INTENT_ACTION_TRIGGER_SYMPTOM_CHECKER";

    private MyAlarmManagerCompat uepaaAlarmManager;

    private static final String TAG = MyService.class.getSimpleName();
    private long wakeUpIntervalMs = 60000;
    private PendingIntent wakeUpPendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wakeUpPendingIntent = buildWakeUpPendingIntent();
        uepaaAlarmManager = new MyAlarmManagerCompat(this);

    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);

        scheduleNextAlarm();

        return Service.START_STICKY;
    }

    public void scheduleNextAlarm() {
        Log.v(TAG, "scheduleAlarms | scheduleeNextAlarm (s) = " + (wakeUpIntervalMs + FIVE_SECONDS) / 1000);
        Toast.makeText(this, "Button pushed for Alarm ", Toast.LENGTH_SHORT).show();

        uepaaAlarmManager.setExactAndAllowWhileIdleOrAlarmClock(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + wakeUpIntervalMs + FIVE_SECONDS, wakeUpPendingIntent);
    }

    private PendingIntent buildWakeUpPendingIntent() {
        final Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(INTENT_ACTION_WAKE_UP);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public class MyServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
}
