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

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextDays, mTextName;
    private RelativeLayout layout_Date, layout_Name;
    private long counterfor;
    private Date date1, date2;
    private Handler uiHandler = new Handler();
    private Vibrator v;
    private Thread t;
    private String eventTime = "07/12/2019 13:00:00";
    private String babyName = "NAME HERE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        mTextDays = ((TextView) findViewById(R.id.textDays));
        mTextName = ((TextView) findViewById(R.id.name_string));
        layout_Date = (RelativeLayout) findViewById(R.id.date_layout);
        layout_Name = (RelativeLayout) findViewById(R.id.layout_name);

        ((RelativeLayout) findViewById(R.id.layout_main))
                .setBackground(getDrawable(R.drawable.hello_me_bg));
        v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            date1 = new Date();
            date2 = simpleDateFormat.parse(eventTime);
            counterfor = date2.getTime() - date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new CountDownTimer(counterfor, 1000) {
            public void onTick(long millisUntilFinished) {
                date1 = new Date();
                printDifference(date1, date2);
            }

            public void onFinish() {
                displayName(babyName);
            }
        }.start();
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
        String countDown = "";
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
            countDownHours = String.format("%02d", elapsedHours) + ":";
        }
        if (elapsedMinutes > 0) {
            countDownMins = String.format("%02d", elapsedMinutes) + ":";
        }
        if (elapsedSeconds > 0) {
            countDownSeconds = String.format("%02d", elapsedSeconds) + "";
        }

        countDown = countDownDays + countDownHours + countDownMins + countDownSeconds;
        mTextDays.setText(countDown);

    }

    public void displayName(String name) {

        layout_Date.setVisibility(View.GONE);
        layout_Name.setVisibility(View.VISIBLE);
        NameCounter runnable = new NameCounter(name);
        t = new Thread(runnable);
        t.start();
    }


    class NameCounter implements Runnable {
        String name = "", animationStr = "";
        int count = 0;

        public NameCounter(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (count < name.length()) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        animationStr = animationStr + " " + name.charAt(count);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ((RelativeLayout) findViewById(R.id.layout_main))
                    .setBackground(getDrawable(R.drawable.hello_me_bg));
            ((TextView) findViewById(R.id.textDays))
                    .setBackground(getDrawable(R.drawable.counter_style));
            ((TextView) findViewById(R.id.textDays))
                    .setTextColor(getResources().getColor(R.color.colorWhite));

        } else {
            ((RelativeLayout) findViewById(R.id.layout_main))
                    .setBackground(getDrawable(R.drawable.hello_me_bg_wide));
            ((TextView) findViewById(R.id.textDays))
                    .setBackground(getDrawable(R.drawable.counter_style_wide));
            ((TextView) findViewById(R.id.textDays))
                    .setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        }
    }

    public void vibEffect() {
        v.vibrate(100);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
