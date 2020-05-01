
package ch.dvbern.alarmtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class AlarmScreenActivity extends Activity {

    private static final String TAG = AlarmScreenActivity.class.getSimpleName();

    private static final String INTENT_EXTRA_CLOSE_NOTIFICATION = "INTENT_EXTRA_CLOSE_NOTIFICATION";
    private static final String INTENT_EXTRA_ALARM_NOTIFICATION = "INTENT_EXTRA_ALARM_NOTIFICATION";

    private NotificationSoundManager soundManager;

    private static Intent buildNotificationStarterIntent(final Context context, final Intent intent) {
        return buildNotificationStarterIntent(context, intent, false);
    }

    public static Intent buildNotificationStarterIntent(final Context context, final Intent intent, final boolean alarmingNotification) {

        intent.putExtra(INTENT_EXTRA_ALARM_NOTIFICATION, alarmingNotification);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        return intent.setClass(context, AlarmScreenActivity.class);
    }

    /**
     * This method opens the Pop-Up in order to close it. Only use this if the popup is shown.
     *
     * @param context
     */
    public static void discardNotification(final Context context) {
        final Intent discardIntent = buildNotificationStarterIntent(context, new Intent());
        discardIntent.putExtra(INTENT_EXTRA_CLOSE_NOTIFICATION, true);
        context.startActivity(discardIntent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Activity LIFECYCLE: onCreate");

        Toast.makeText(this, "Alarm Activity ", Toast.LENGTH_SHORT).show();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.fragment_alarm);

        this.soundManager = new NotificationSoundManager(this);
        makeRequestedNotificationSound(soundManager);
    }

    private void makeRequestedNotificationSound(final NotificationSoundManager soundManager) {
        if (getIntent() == null || getIntent().getExtras() == null) {
            return;
        }
        if (getIntent().getExtras().getBoolean(INTENT_EXTRA_ALARM_NOTIFICATION)) {
            //soundManager.startAlarmNotification(R.raw.alarming_notification_sound);
            soundManager.vibrate();
        } else if (getIntent().getExtras().getBoolean(INTENT_EXTRA_CLOSE_NOTIFICATION)) {
            // Make No Sound Here
            return;
        } else {
            soundManager.startNotification();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Activity LIFECYCLE: onResume");
        final Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            // Keep the current GUI state
            return;
        }
        if (intent.getExtras().getBoolean(INTENT_EXTRA_CLOSE_NOTIFICATION)) {
            finish();
            return;
        }

        final NotificationType notificationType = NotificationType.valueOf(intent.getExtras().getInt(
                NotificationService.INTENT_EXTRA_NOTIFICATION_TYPE));
        Log.d(TAG, "Showing Notification for NotificationType = " + notificationType);
        //displayTextAccordingTo(notificationType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Activity LIFECYCLE: onDestroy");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity LIFECYCLE: onDestroy");
        soundManager.release();
    }

//    private void displayTextAccordingTo(final NotificationType type) {
//        ignoreButton.setVisibility(View.GONE);
//        openButton.setText(R.string.open_uepaa);
//        switch (type) {
//            case BAD_SYMPTOM_MISSION_REQUESTED:
//                titleView.setText(R.string.buddy_aid_notification_title);
//                centerText.setText(R.string.bad_symptom_request_notify);
//                break;
//            case MISSION_CANCELED:
//                titleView.setText(R.string.buddy_aid_notification_title);
//                centerText.setText(R.string.bad_symptom_canceled_notify);
//                break;
//            case BAD_SYMPTOM_DETECTED:
//                titleView.setText(getBadSymptomTitleId());
//                centerText.setText(R.string.symptom_check_detected_notify);
//                break;
//            case BAD_SYMPTOM_PUBLISHED:
//                titleView.setText(getBadSymptomTitleId());
//
//                String message = getResources().getString(R.string.symptom_check_published_notify_responder);
//                String recipient = new EmergencyInfoStorage(this).loadModel(this).formatRespondersByFirstName();
//
//                PremiumStorage premiumStorage = PremiumStorage.getInstance(this);
//                if (premiumStorage.isMandatorUser() && premiumStorage.hasMandatorInfo()) {
//                    MandatorInfo mandatorInfo = premiumStorage.loadMandatorInfo(this, false);
//                    if (SymptomProcessType.ESCALATE_TO_ALERT == mandatorInfo.getSymptomProcessType()) {
//                        message = getResources().getString(R.string.symptom_check_published_notify_ec);
//                        recipient = getResources().getString(R.string.emergency_center);
//                    } else if (SymptomProcessType.FORWARD_TO_MOBILE == mandatorInfo.getSymptomProcessType()) {
//                        message = getResources().getString(R.string.symptom_check_published_notify_responder);
//                        recipient = mandatorInfo.getEscalationMobileName();
//                    }
//                }
//
//                centerText.setText(message.replace("{RECIPIENT}", recipient));
//
//                break;
//            case BAD_SYMPTOM_CONFIRMED:
//                titleView.setText(getBadSymptomTitleId());
//                centerText.setText(getResources().getString(R.string.symptom_check_confirmed_notify));
//                break;
//            case BUDDY_AID_CONFIRMED:
//                titleView.setText(R.string.buddy_aid_notification_title);
//                centerText.setText(R.string.premium_alert_buddyaid_confiremd_notify);
//                break;
//            case BUDDY_AID_MISSION_REQUESTED:
//                titleView.setText(R.string.buddy_aid_notification_title);
//                centerText.setText(R.string.buddy_aid_request_notify);
//            default:
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed | back button is routed ot the app by design");
        openApp();
    }

    private void openApp() {

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtras(getIntent()); // pass INTENT_EXTRA_NOTIFICATION_TYPE
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

        finish();
    }

}
