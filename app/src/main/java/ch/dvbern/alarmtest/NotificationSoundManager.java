
package ch.dvbern.alarmtest;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import java.io.IOException;

class NotificationSoundManager {
    private static final int AUDIO_STREAM = AudioManager.STREAM_ALARM;
    private final static String TAG = NotificationSoundManager.class.getSimpleName();
    private static final int NR_OF_SOUND_REPEATINGS = 2;
    private final Activity context;
    private final AudioManager audioManager;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private int originalStreamVolume;
    private OnAudioFocusChangeListener onAudioFocusChangeListener;
    private int soundPlayCount = 0;

    public NotificationSoundManager(final Activity context) {
        this.context = context;
        this.audioManager = initializeAudioManager();
    }

    public boolean startAlarmNotification(final int resourceId) {
        setAudioVolume();
        if (getAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            playAudioAlarm(resourceId);
            return true;
        }
        return false;
    }

    public void startNotification() {
        if (getAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            final Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final Ringtone r = RingtoneManager.getRingtone(context, notification);
            if (r != null) {
                r.play();
            } else {
                playCustomTones();
            }
            vibrate(false);
        }
    }

    public void release() {
        releaseMediaPlayer(mediaPlayer);
        stopVibrator();
        audioManager.setStreamVolume(AUDIO_STREAM, originalStreamVolume, 0);
        if (onAudioFocusChangeListener != null) {
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    private void releaseMediaPlayer(final MediaPlayer mp) {
        if (mp != null) {
            mp.release();
        }
    }

    public void vibrate() {
        vibrate(true);
    }

    public void vibrate(final boolean repeat) {
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] { 0, 200, 100, 200, 100, 200, 100, 500, 100, 500, 100, 500, 100, 200, 100, 200,
                100, 200, 1000 }, repeat ? 0 : -1);
    }

    private void stopVibrator() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private AudioManager initializeAudioManager() {
        final AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        context.setVolumeControlStream(AUDIO_STREAM);
        this.originalStreamVolume = am.getStreamVolume(AUDIO_STREAM);
        return am;
    }

    private int getAudioFocus() {
        this.onAudioFocusChangeListener = new OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(final int focusChange) {
                // audioManager.setStreamVolume(AUDIO_STREAM, originalStreamVolume, 0);
            }
        };
        final int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AUDIO_STREAM,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        return result;
    }

    private MediaPlayer createMediaPlayerWithAudioStream(final int resourceId) throws IOException {
        final AssetFileDescriptor afd = context.getResources().openRawResourceFd(resourceId);
        if (afd == null) {
            throw new IllegalArgumentException("Can't find resource");
        }
        final MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
        mp.setAudioStreamType(AUDIO_STREAM);
        mp.prepare();
        return mp;
    }

    private void playAudioAlarm(final int resourceId) {
        try {
            this.mediaPlayer = createMediaPlayerWithAudioStream(resourceId);
        } catch (Exception e) {
            Log.e(TAG, "Failed to create MediaPlayer for Audio Alarm", e);

            return;
        }
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(final MediaPlayer mp) {
                if (soundPlayCount >= NR_OF_SOUND_REPEATINGS) {
                    releaseMediaPlayer(mp);
                    stopVibrator();
                    soundPlayCount = 0;
                } else {
                    soundPlayCount++;
                    mediaPlayer.start();
                }
            }
        });
        soundPlayCount = 1;
        mediaPlayer.start();
    }

    private void setAudioVolume() {
        final int streamMaxVolume = audioManager.getStreamMaxVolume(AUDIO_STREAM);
        audioManager.setStreamVolume(AUDIO_STREAM, streamMaxVolume, 0);
        Log.d(TAG,
            "UPA_SOUND: Setting Volume to " + streamMaxVolume + " of " + audioManager.getStreamMaxVolume(AUDIO_STREAM));
    }

    private void playCustomTones() {
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                playTones(new int[] { ToneGenerator.TONE_DTMF_A, ToneGenerator.TONE_DTMF_B, ToneGenerator.TONE_DTMF_C,
                        ToneGenerator.TONE_DTMF_D, ToneGenerator.TONE_DTMF_C, ToneGenerator.TONE_DTMF_B,
                        ToneGenerator.TONE_DTMF_A });
            }
        });
        thread.start();
    }

    private void playTones(final int[] tones) {
        try {
            // Getting the user sound settings
            final int currentRingerVolume = (int) Math.ceil(((float) audioManager
                .getStreamVolume(AudioManager.STREAM_RING) / (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_RING)) * 100);
            final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_RING, currentRingerVolume);
            for (int tone : tones) {
                toneGenerator.startTone(tone, 120);
                Thread.sleep(100);
            }
            toneGenerator.stopTone();
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }
    }
}
