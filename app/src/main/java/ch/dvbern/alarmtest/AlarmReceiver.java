
package ch.dvbern.alarmtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Toast.makeText(context, "AlarmReceiver onReceive ", Toast.LENGTH_SHORT).show();

        if (intent == null) {
            Log.w(TAG, "onReceive | Received intent = NULL.");
            return;
        }

        Log.d(TAG, "onReceive | action = " + intent.getAction());

        if (MyService.INTENT_ACTION_WAKE_UP.equals(intent.getAction())) {
            doWork(context);
            //AlarmServiceHelper.triggerSymptomCheckAlarm(context);
        }
    }

    public void doWork(final Context context) {

        MyNotificationManager.notifyAlarm(context, NotificationType.BAD_SYMPTOM_DETECTED);

    }

}
