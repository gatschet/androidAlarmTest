
package ch.dvbern.alarmtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

public class MyAlarmManagerCompat {

    private final AlarmManager mAlarmManager;
    private final PowerManager mPowerManager;
    private static final String TAG = MyAlarmManagerCompat.class.getSimpleName();

    private static final String[] MANUFACTURER_WITH_STUPID_ALARMMANAGER = {"samsung", "HUAWEI"};

    /**
     * Creates a new system service alarm manager.
     *
     * @param context Context.
     */
    public MyAlarmManagerCompat(Context context) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    /**
     * Set alarm at exact time.
     * Note: Minimum interval is 15min.
     *
     * @param type            Type.
     * @param triggerAtMillis Fire date.
     * @param operation       Intent.
     */
    @SuppressWarnings("NewApi")
    public void setExactAndAllowWhileIdle(final int type, final long triggerAtMillis, final PendingIntent operation) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setExactAndAllowWhileIdle(type, triggerAtMillis, operation);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(type, triggerAtMillis, operation);
        } else {
            mAlarmManager.set(type, triggerAtMillis, operation);
        }
    }

    /**
     * Set alarm at exact time when interactive or set alarm clock when not interactive.
     *
     * @param type            Type.
     * @param triggetAtMillis Fire date.
     * @param operation       Intent.
     */
    public void setExactAndAllowWhileIdleOrAlarmClockWhenNotInteractive(final int type, final long triggetAtMillis, final PendingIntent operation) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P
                && !mPowerManager.isInteractive() && isManufacturerWithStupidAlarmManager()) {
            Log.v(TAG, "setExactAndAllowWhileIdleOrAlarmClockWhenNotInteractive | using setAlarmClock = " + triggetAtMillis);
            mAlarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(triggetAtMillis, operation), operation);
        } else {
            Log.v(TAG, "setExactAndAllowWhileIdleOrAlarmClockWhenNotInteractive | using setExactAndAllowWhileIdle = " + triggetAtMillis);
            setExactAndAllowWhileIdle(type, triggetAtMillis, operation);
        }
    }

    /**
     * Set alarm at exact time when interactive or set alarm clock when not interactive.
     *
     * @param type            Type.
     * @param triggetAtMillis Fire date.
     * @param operation       Intent.
     */
    public void setExactAndAllowWhileIdleOrAlarmClock(final int type, final long triggetAtMillis, final PendingIntent operation) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isManufacturerWithStupidAlarmManager()) {
            Log.v(TAG, "setExactAndAllowWhileIdleOrAlarmClock | using setAlarmClock = " + triggetAtMillis);
            mAlarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(triggetAtMillis, operation), operation);
        } else {
            Log.v(TAG, "setExactAndAllowWhileIdleOrAlarmClock | using setExactAndAllowWhileIdle = " + triggetAtMillis);
            setExactAndAllowWhileIdle(type, triggetAtMillis, operation);
        }
    }

    private boolean isManufacturerWithStupidAlarmManager() {

        String manufacturer = Build.MANUFACTURER;

        for (String manufacturerWithStupidAlarmmanager : MANUFACTURER_WITH_STUPID_ALARMMANAGER) {
            if (manufacturerWithStupidAlarmmanager.equals(manufacturer)) {
                Log.v(TAG, "isManufacturerWithStupidAlarmManager | is's Manufacturer with stupid AlarmManager" + manufacturer);
                return true;
            }
        }

        return false;
    }

    /**
     * Set alarm at time (batched).
     * Note: Minimum interval is 15min.
     *
     * @param type            Type.
     * @param triggerAtMillis Fire date.
     * @param operation       Intent.
     */
    @SuppressWarnings("NewApi")
    public void setAndAllowWhileIdle(final int type, final long triggerAtMillis, final PendingIntent operation) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setAndAllowWhileIdle(type, triggerAtMillis, operation);
        } else {
            mAlarmManager.set(type, triggerAtMillis, operation);
        }
    }

    /**
     * Set alarm at time.
     * Note: Will not wake up device from doze.
     *
     * @param type            Type.
     * @param triggerAtMillis Fire date.
     * @param operation       Intent.
     */
    public void set(final int type, final long triggerAtMillis, final PendingIntent operation) {
        mAlarmManager.set(type, triggerAtMillis, operation);
    }

    /**
     * Set exact alarm time.
     * Note: Will not wake up device from doze.
     *
     * @param type            Type.
     * @param triggetAtMillis Fire date.
     * @param operation       Intent.
     */
    public void setExact(final int type, final long triggetAtMillis, final PendingIntent operation) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(type, triggetAtMillis, operation);
        } else {
            mAlarmManager.set(type, triggetAtMillis, operation);
        }
    }

    /**
     * Set exact alarm time when interactive or set alarm clock when not interactive.
     * Note: Will not wake up device from doze.
     *
     * @param type            Type.
     * @param triggetAtMillis Fire date.
     * @param operation       Intent.
     */
    public void setExactOrAlarmClockWhenNotInteractive(final int type, final long triggetAtMillis, final PendingIntent operation) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N  && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P
                && !mPowerManager.isInteractive() && isManufacturerWithStupidAlarmManager()) {
            Log.v(TAG, "setExactOrAlarmClockWhenNotInteractive | using setAlarmClock = " + triggetAtMillis);
            mAlarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(triggetAtMillis, operation), operation);
        } else {
            Log.v(TAG, "setExactOrAlarmClockWhenNotInteractive | using setExact = " + triggetAtMillis);
            setExact(type, triggetAtMillis, operation);
        }
    }

    /**
     * Set repeating alarm.
     * Note: Will not wake up device from doze.
     *
     * @param type            Type.
     * @param triggerAtMillis Fire date.
     * @param intervalMillis  Repeat interval.
     * @param operation       Intent.
     */
    public void setRepeating(final int type, final long triggerAtMillis, final long intervalMillis, final PendingIntent operation) {
        mAlarmManager.setRepeating(type, triggerAtMillis, intervalMillis, operation);
    }

    /**
     * Cancels a scheduled alarm.
     *
     * @param intent Scheduled intent.
     */
    public void cancel(final PendingIntent intent) {
        mAlarmManager.cancel(intent);
    }
}
