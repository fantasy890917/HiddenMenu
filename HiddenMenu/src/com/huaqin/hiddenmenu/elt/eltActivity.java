package com.huaqin.hiddenmenu.elt;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.*;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import com.huaqin.hiddenmenu.R;

public class eltActivity extends Activity {
    private static final String TAG = "eltActivity";

    private static final int LCDCOUNT = 10;//10
    private static final int RINGTONETIME = 900000;//900000
    private static final int VIBRATORCOUNT = 300;//300
    private static final int CAMERACOUNT = 10;//10

    private static final int LCD_ONOROFF_TIME = 1000;
    private static final int VIBRATOR_ONANDOFF_TIME = 3000;
    private static final int VIBRAT_ON_TIME = 2000;

    private static final int CAMERA_ONOROFF_TIME = 8000;
    private static final int CAMERA_OFF_TIME = 2000;
    private static final int CAMERAPREVIEW_TIME = 900000;//900000

    private int MSGID_TESTCASE;
    private int MSGID_PASSTIME;
    private String para = "1";

    private long delay;
    private long period;
    private int countall;
    private int count;

    private int testItemCount;
    private boolean testItemStatus;
    private int passTimes;

    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    PowerManager powerManager = null;
    Vibrator vibrator = null;
    WakeLock wakeLock = null;

    Intent intent = null;
    private Handler mHandler = null;
    private Handler mTime_Handler = null;
    private Timer mTestTimer = null;
    private TimerTask mTestTimerTask = null;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;


    private TextView countinfo_TextView;
    private TextView item_TextView;
    private TextView itemInfo_TextView;
    private TextView statusInfo_TextView;
    private TextView timeInfo_TextView;


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

    private void startTimer(int index) {
        if (mTimer == null) {
            mTimer = new Timer();
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
        if ((mTimer != null) && (mTestTimerTask != null)) {
            switch (index) {
                case 1:
                    delay = LCD_ONOROFF_TIME;
                    period = LCD_ONOROFF_TIME;
                    break;
                case 2:
                    delay = RINGTONETIME;
                    period = RINGTONETIME;
                    break;
                case 3:
                    delay = VIBRATOR_ONANDOFF_TIME;
                    period = VIBRATOR_ONANDOFF_TIME;
                    break;
                case 4:
                    delay = CAMERA_ONOROFF_TIME;
                    period = CAMERA_ONOROFF_TIME;
                    break;
                case 5:
                    delay = CAMERAPREVIEW_TIME;
                    period = CAMERAPREVIEW_TIME;
                    break;
                default:
                    break;
            }
            mTimer.schedule(mTestTimerTask, delay, period);
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTestTimerTask != null) {
            mTestTimerTask.cancel();
            mTestTimerTask = null;
        }
    }

    private void elapsedTime(int index) {
        if (mTestTimer == null) {
            mTestTimer = new Timer();
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
        if ((mTestTimer != null) && (mTimerTask != null)) {
            mTestTimer.schedule(mTimerTask, 1000, 1000);
        }
    }

    private void endTime() {
        if (mTestTimer != null) {
            mTestTimer.cancel();
            mTestTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public void lcmOn() {
        if (powerManager == null) {
            powerManager = ((PowerManager) getSystemService("power"));
        }
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
        setContentView(R.layout.activity_elt);

        item_TextView = ((TextView) findViewById(R.id.item_textView));
        itemInfo_TextView = ((TextView) findViewById(R.id.iteminfo_textView));
        statusInfo_TextView = ((TextView) findViewById(R.id.statusinfo_textView));
        timeInfo_TextView = ((TextView) findViewById(R.id.timer_textView));
        countinfo_TextView = ((TextView) findViewById(R.id.countinfo_textView));
        intent = getIntent();
        para = intent.getStringExtra("elt_count");
        countall = Integer.parseInt(para);
        Log.i("hiddenMenu", "countall===>" + para);
        init();
        elapsedTime(countall);
    }

    protected void onDestroy() {
        stopELT();
        count = 0;
        lcmOff();
        endTime();
        super.onDestroy();
    }

    private void init() {
        lcmOn();
        count = 1;
        testItemCount = 1;
        testItemStatus = false;
        mHandler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        lcdBrightness(testItemStatus);
                        testItemStatus = !testItemStatus;
                        if (testItemCount == LCDCOUNT) {
                            stopTimer();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    item_TextView.setText(R.string.elt_item_ringtone);
                                    itemInfo_TextView.setText("");
                                    playBGSound();
                                    startTimer(2);
                                }
                            },1000);
                        }
                        break;
                    case 2:
                        stopTimer();
                        stopSound();
                        item_TextView.setText(R.string.elt_item_vibrator);
                        itemInfo_TextView.setText("");
                        testItemCount = 1;
                        setVibrate(VIBRAT_ON_TIME);
                        startTimer(3);
                        break;
                    case 3:
                        if (testItemCount == VIBRATORCOUNT) {
                            testItemCount = 1;

                            item_TextView.setText(R.string.elt_item_enter_camera);
                            itemInfo_TextView.setText(Integer.toString(testItemCount) + "/" + CAMERACOUNT);
                            statusInfo_TextView.setText(R.string.elt_ON);

                            stopTimer();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    enterCamera();
                                    startTimer(4);
                                }
                            }, 1000);
                            break;
                        }

                        testItemCount += 1;
                        setVibrate(VIBRAT_ON_TIME);
                        break;
                    case 4:
                        exitCamera();
                        if(testItemCount == CAMERACOUNT){
                            stopTimer();

                            startTimer(5);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    item_TextView.setText(R.string.elt_item_camera_preview);
                                    itemInfo_TextView.setText("");
                                    enterCamera();
                                }
                            }, 1000);
                            break;
                        }

                        testItemCount++;
                        itemInfo_TextView.setText(Integer.toString(testItemCount) + "/" + CAMERACOUNT);

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                enterCamera();
                            }
                        }, CAMERA_OFF_TIME);
                        break;
                    case 5:
                        exitCamera();
                        stopTimer();
                        if(count == countall) {
                            item_TextView.setText("");
                            itemInfo_TextView.setText(R.string.elt_finished);
                            statusInfo_TextView.setText(R.string.elt_OFF);
                        }else{
                            testItemCount = 1;
                            testItemStatus = false;
                            count++;
                            countinfo_TextView.setText(Integer.toString(count) + "/" + Integer.toString(countall));
                            itemInfo_TextView.setText(Integer.toString(testItemCount) + "/" + LCDCOUNT);
                            startTimer(1);
                        }
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

        countinfo_TextView.setText(Integer.toString(count) + "/" + Integer.toString(countall));
        itemInfo_TextView.setText(Integer.toString(testItemCount) + "/" + LCDCOUNT);
        startTimer(1);
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
            testItemCount += 1;
            statusInfo_TextView.setText(R.string.elt_ON);
            itemInfo_TextView.setText(Integer.toString(testItemCount) + "/" + LCDCOUNT);
            powerManager.setBacklightOffForWfd(false);
        }
    }

    private void playBGSound() {
        statusInfo_TextView.setText(R.string.elt_Play);
        testItemCount += 1;
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
            },time);
        } else {
            vibrator.cancel();
        }
        statusInfo_TextView.setText(R.string.elt_ON);


    }

    private void enterCamera() {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    private void exitCamera() {
        Intent intent = new Intent();
        intent.setClass(this, eltActivity.class);
        startActivity(intent);
    }

    private void stopELT() {
        stopTimer();
        lcdBrightness(true);
        stopSound();
        setVibrate(0);
    }
}

