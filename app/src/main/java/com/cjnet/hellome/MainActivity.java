package com.cjnet.hellome;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextDays;
    private TextView mTextName;
    private RelativeLayout layoutDate;
    private RelativeLayout layoutName;
    private long counterFor;
    private Date date1;
    private Date date2;
    private Handler uiHandler = new Handler();
    private Vibrator vibrator;
    private String babyName = "NAME";
    private volatile boolean exited;
    private static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        //WHAT IS THE USE OF ACTION AND DATA HERE?
        //FROM WHERE YOU ARE EXPECTING THESE VALUES?
        String action = intent.getAction();
        Uri data = intent.getData();
        String eventTime = "14/12/2019 19:05:00";

        mTextDays = findViewById(R.id.textDays);
        mTextName = findViewById(R.id.name_string);
        layoutDate = findViewById(R.id.date_layout);
        layoutName = findViewById(R.id.layout_name);

        findViewById(R.id.layout_main)
                .setBackground(getDrawable(R.drawable.hello_me_bg));
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        try {
            date1 = new Date();
            date2 = simpleDateFormat.parse(eventTime);
            counterFor = date2.getTime() - date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        countDownTimer = new CountDownTimer(counterFor, 1000) {
            public void onTick(long millisUntilFinished) {
                date1 = new Date();
                printDifference(date1, date2);
            }

            public void onFinish() {
                displayName(babyName);
            }
        };

        countDownTimer.start();
    }


    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        String countDown;
        String countDownDays = "";
        String countDownHours = "";
        String countDownMins = "00:";
        String countDownSeconds = "00";

        if (elapsedDays > 0) {
            if (elapsedDays == 1) {
                countDownDays = elapsedDays + " day ";
            } else {
                countDownDays = elapsedDays + " days ";
            }
        }
        if (elapsedHours > 0) {
            countDownHours = String.format(Locale.getDefault(), "%02d", elapsedHours) + ":";
        }
        if (elapsedMinutes > 0) {
            countDownMins = String.format(Locale.getDefault(), "%02d", elapsedMinutes) + ":";
        }
        if (elapsedSeconds > 0) {
            countDownSeconds = String.format(Locale.getDefault(), "%02d", elapsedSeconds) + "";
        }

        countDown = countDownDays + countDownHours + countDownMins + countDownSeconds;
        mTextDays.setText(countDown);
    }

    public void displayName(String name) {
        layoutDate.setVisibility(View.GONE);
        layoutName.setVisibility(View.VISIBLE);
        NameCounter runnable = new NameCounter(name);
        Thread t = new Thread(runnable);
        t.start();
    }


    private class NameCounter implements Runnable {
        String name;
        StringBuilder animationStr = new StringBuilder();
        int count = 0;

        NameCounter(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (count < name.length()) {
                if (!exited) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            animationStr = animationStr.append(" ").append(name.charAt(count));
                            mTextName.setText(animationStr);
                            vibEffect();
                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        Log.e("ERROR", "ERROR-HERE");
                    }
                    count++;
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.layout_main)
                    .setBackground(getDrawable(R.drawable.hello_me_bg));
            findViewById(R.id.textDays)
                    .setBackground(getDrawable(R.drawable.counter_style));
            ((TextView) findViewById(R.id.textDays))
                    .setTextColor(getResources().getColor(R.color.colorWhite));

        } else {
            findViewById(R.id.layout_main)
                    .setBackground(getDrawable(R.drawable.hello_me_bg_wide));
            findViewById(R.id.textDays)
                    .setBackground(getDrawable(R.drawable.counter_style_wide));
            ((TextView) findViewById(R.id.textDays))
                    .setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void vibEffect() {
        vibrator.vibrate(100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        exited = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer = null; //to avoid memory leak
    }

    @Override
    protected void onResume() {
        super.onResume();
        exited = false;
    }
}
