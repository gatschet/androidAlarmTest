
package ch.dvbern.alarmtest;

import android.content.Context;
import android.content.Intent;

public final class AlarmServiceHelper {

    private static final String TAG = AlarmServiceHelper.class.getSimpleName();

    private AlarmServiceHelper() {
    }


    public static void triggerSymptomCheckAlarm(final Context context) {
        final Intent intent = new Intent(context, MyService.class);
        intent.setAction(MyService.INTENT_ACTION_TRIGGER_SYMPTOM_CHECKER);
        context.startService(intent);
    }

}