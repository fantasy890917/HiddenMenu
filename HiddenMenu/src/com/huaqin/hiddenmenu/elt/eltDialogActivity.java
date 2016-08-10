package com.huaqin.hiddenmenu.elt;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.*;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import com.huaqin.hiddenmenu.R;
public class eltDialogActivity extends Activity {
    private static final String TAG = "eltDialogActivity";
    private int passTimes;
    private int MSGID_PASSTIME;
    private int MSGID_TESTCASE;

    private boolean status = false;
    String para = "1";

    private int testCount;
    private long delay;
    private long period;
    private int testCase;

    Intent mIntent = null;
    Handler mHandler = null;
    Handler mTime_Handler = null;

    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    PowerManager powerManager = null;
    Vibrator vibrator = null;
    WakeLock wakeLock = null;
    Timer mTestTimer = null;
    Timer mTimer = null;
    TimerTask mTestTimerTask = null;
    TimerTask mTimerTask = null;


    TextView testCount_TextView;
    TextView testName_TextView;
    TextView status_TextView;
    TextView statusInfo_TextView;
    TextView time_TextView;
    TextView timeInfo_TextView;
    Button stop_Button;


    private void startTimer(int index) {
        if (mTestTimer == null) {
            mTestTimer = new Timer();
        }
        MSGID_TESTCASE = index;
        if (mTestTimerTask == null) {
            mTestTimerTask = new TimerTask() {
                public void run() {
                    Message message = new Message();
                    message.what = MSGID_TESTCASE;
                    mHandler.sendMessage(message);
                }
            };
        }
        if ((mTestTimer != null) && (mTestTimerTask != null)) {
            mTestTimer.schedule(mTestTimerTask, delay, period);
        }
    }

    private void stopTimer() {
        if (mTestTimer != null) {
            mTestTimer.cancel();
            mTestTimer = null;
        }
        if (mTestTimerTask != null) {
            mTestTimerTask.cancel();
            mTestTimerTask = null;
        }
    }

    private void elapsedTime(int index) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        MSGID_PASSTIME = index;
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                public void run() {
                    Message message = new Message();
                    message.what = MSGID_PASSTIME;
                    mTime_Handler.sendMessage(message);
                }
            };
        }
        if ((mTimer != null) && (mTimerTask != null)) {
            mTimer.schedule(mTimerTask, 1000, 1000);
        }
    }

    private void endTime() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void showTime(int time) {
	 String html ="0秒";
         if(time != 0){
             String format;
             Object[] array;
             Integer hours =(int) (time/(60*60));
             Integer minutes = (int) (time/60-hours*60);
             Integer seconds = (int) (time-minutes*60-hours*60*60);
             if(hours > 0){
                 format="%1$,d时%2$,d分%3$,d秒";
                 array=new Object[]{hours,minutes,seconds};
             }else if(minutes > 0){
                 format="%1$,d分%2$,d秒";
                 array=new Object[]{minutes,seconds};
             }else{
                 format="%1$,d秒";
                 array=new Object[]{seconds};
             }
             html= String.format(format, array);
         }
        timeInfo_TextView.setText(html);
    }

    public void lcmOn() {
        powerManager = ((PowerManager) getSystemService("power"));
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TEST");
        wakeLock.acquire();
    }

    private void lcmOff() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(R.layout.elt_dialog_activity);
        setFinishOnTouchOutside(false);
        testName_TextView = ((TextView) findViewById(R.id.item_textTitle));
        status_TextView = ((TextView) findViewById(R.id.status_textView));
        statusInfo_TextView = ((TextView) findViewById(R.id.statusinfo_textView));
        time_TextView = ((TextView) findViewById(R.id.timertitle_textView));
        timeInfo_TextView = ((TextView) findViewById(R.id.timer_textView));
        testCount_TextView = ((TextView) findViewById(R.id.countinfo_textView));
        stop_Button = ((Button) findViewById(R.id.stopbutton));
        stop_Button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        mIntent = getIntent();
        para = mIntent.getStringExtra("elt_manual_order");
        testCase = Integer.parseInt(para);
        Log.i("elt_manual_order", "testCase===>" + para);
        init();
        elapsedTime(testCase);
    }

    protected void onDestroy() {
        testCase = 0;
        testCount = 0;
        lcmOff();
        endTime();
        stopTimer();
        setVibrate(0);
        lcdBrightness(true);
        stopSound();
        super.onDestroy();
    }

    private void init() {
        Log.i("elt_manual_order", "init===>");
        lcmOn();
        testCount = 1;
        testCount_TextView.setText(Integer.toString(testCount));
        showTime(0);

        mHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        lcdBrightness(status);
                        status = !status;
                        break;
                    case 2:
                        stopSound();
                        playBGSound();
                        break;
                    case 3:
                        setVibrate(2000);
                        break;
                    case 4:
                        break;
                }
            }
        };

        mTime_Handler = new Handler() {
            public void handleMessage(Message message) {
                passTimes++;
                showTime(passTimes);
            }
        };

        switch (testCase) {
            case 1:
                Log.d(TAG, "begin LCD TEST");
                testName_TextView.setText(R.string.elt_item_backlight);
                delay = 1000;
                period = 1000;
                startTimer(testCase);
                break;
            case 2:
                Log.d(TAG, "begin Ringtone TEST");
                testName_TextView.setText(R.string.elt_ringtone);
                delay = 16000;
                period = 16000;
                playBGSound();
                startTimer(testCase);
                break;
            case 3:
                Log.d(TAG, "begin Vibrator TEST");
                testName_TextView.setText(R.string.vib_title);
                setVibrate(2000);
                delay = 3000;
                period = 3000;
                startTimer(testCase);
                break;
            case 4:
                Log.d(TAG, "begin Camera TEST");
                testName_TextView.setText(R.string.elt_item_enter_camera);
                enterCamera();
                finish();
                break;
            default:
                break;
        }
    }

    private void lcdBrightness(boolean enable) {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        if (powerManager == null) {
            powerManager = ((PowerManager) getSystemService("power"));
        }
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TEST");
        wakeLock.acquire();

        if (enable == false) {
            statusInfo_TextView.setText(R.string.elt_OFF);
            powerManager.setBacklightOffForWfd(true);
        } else {
            testCount += 1;
            statusInfo_TextView.setText(R.string.elt_ON);
            testCount_TextView.setText(Integer.toString(testCount));
            powerManager.setBacklightOffForWfd(false);
        }
    }

    private void playBGSound() {
        statusInfo_TextView.setText(R.string.elt_Play);
        testCount_TextView.setText(Integer.toString(testCount));
        testCount += 1;
        mediaPlayer = MediaPlayer.create(this, R.raw.beyondream);
        audioManager = ((AudioManager) getSystemService("audio"));
        if (mediaPlayer == null) {
            Log.e(TAG, "mediaPlayer == NULL");
            return;
        }
        try {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            audioManager.adjustVolume(1, 0);
        } catch (Exception e) {
            Log.e(TAG, "mediaPlayer Exception" + e.toString());
            e.printStackTrace();
        }
    }

    private void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void setVibrate(int time) {
        vibrator = ((Vibrator) getSystemService("vibrator"));
        if (time != 0) {
            vibrator.vibrate(time);
            Log.e(TAG, "Vibrate" + time);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    statusInfo_TextView.setText(R.string.elt_OFF);
                }
            }, time);
        } else {
            vibrator.cancel();
        }
        statusInfo_TextView.setText(R.string.elt_ON);
        testCount_TextView.setText(Integer.toString(testCount));
        testCount += 1;
    }

    private void enterCamera() {
        Log.e(TAG, "enterCamera");
        statusInfo_TextView.setText(R.string.elt_ON);
        testCount_TextView.setText(Integer.toString(testCount));

        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
        testCount += 1;
    }
}

