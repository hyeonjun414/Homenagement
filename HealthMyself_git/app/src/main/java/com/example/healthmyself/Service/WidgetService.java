package com.example.healthmyself.Service;

import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.healthmyself.Activity.MainActivity;
import com.example.healthmyself.Fragment.FragmentMainTimer_Java;
import com.example.healthmyself.R;

import java.util.Calendar;

public class WidgetService extends Service {
    int LAYOUT_FLAG;
    View mFloatingView;
    View test;
    WindowManager windowManager;
    ImageView imageClose;
    TextView tvWidget;
    CountDownTimer countDownTimer;
    int endTime = 250;
    int flag = 0;
    String original_time = "";
    float height,width;
    IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public WidgetService getService() { // 서비스 객체를 리턴
            return WidgetService.this;
        }
        //프래그먼트랑 소통하기 위한 서비스 객체 생성
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    //통신응 위한 객체 반환


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //inflat widget layout
        original_time = intent.getStringExtra("name");
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_widget,null);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //test = inflater.inflate(R.layout.fragment_main_timer, null);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        //initial position
        layoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
        layoutParams.x = 0;
        layoutParams.y = 100;


        //layout params for close button
        WindowManager.LayoutParams imageParams = new WindowManager.LayoutParams(140,
                140,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        imageParams.gravity = Gravity.BOTTOM|Gravity.CENTER;
        imageParams.y = 100;

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        imageClose = new ImageView(this);
        imageClose.setImageResource(R.drawable.close_white);
        imageClose.setVisibility(View.INVISIBLE);
        windowManager.addView(imageClose, imageParams);
        windowManager.addView(mFloatingView, layoutParams);
        mFloatingView.setVisibility(View.VISIBLE);

        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();

        tvWidget = (TextView)mFloatingView.findViewById(R.id.text_widget);

        //show & update current time in textview

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvWidget.setText(original_time);
            }
        }, 100);

        //drag movement for widgets
        tvWidget.setOnTouchListener(new View.OnTouchListener(){
            int initialX, initialY;
            float initialTouchX, initialTouchY;
            long startClickTime;
            long dropClickTime;
            int MAX_CLICK_DURATION=400;
            int MIN_CLICK_DURATION=100;



            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {

                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;

                        long double_tab = Calendar.getInstance().getTimeInMillis() - dropClickTime;

                        if(double_tab < MIN_CLICK_DURATION){
                            countDownTimer.cancel();
                            tvWidget.setText(original_time);
                            flag = 1;
                            tvWidget.setBackgroundResource(R.drawable.back_circle);
                        }

                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();


                        return true;
                    case MotionEvent.ACTION_UP:
                        imageClose.setVisibility(View.GONE);

                        dropClickTime = Calendar.getInstance().getTimeInMillis();

                        long clickDuration = Calendar.getInstance().getTimeInMillis()-startClickTime;


                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);

                        if(clickDuration < MAX_CLICK_DURATION)
                        {
                            if(flag == 0){
                                fn_countdown();
                                flag = 1;
                            }
                            else if (flag == 1)
                            {
                                countDownTimer.cancel();
                                flag = 0;
                            }

                        }else{
                            if(layoutParams.y > (height*0.6))
                            {
                                stopSelf();
                            }
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        imageClose.setVisibility(View.VISIBLE);
                        //claculate X & Y cordinates of view
                        layoutParams.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
                        layoutParams.y = initialY + (int)(motionEvent.getRawY() - initialTouchY);

                        //update layout with new condinates
                        windowManager.updateViewLayout(mFloatingView, layoutParams);

                        if(layoutParams.y > (height*0.6))
                        {
                            imageClose.setImageResource(R.drawable.close_black);
                        }else{
                            imageClose.setImageResource(R.drawable.close_white);
                        }


                        return true;
                }

                return false;
            }
        });


        return START_STICKY;
    }



    private void fn_countdown() {
        String timeInterval = tvWidget.getText().toString();
        endTime = time_to_seconds(timeInterval);


        countDownTimer = new CountDownTimer(endTime * 1000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                int milli_seconds = (int) (millisUntilFinished % 100);
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int min = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                String newtime = min + ":" + seconds + ":" + milli_seconds;
                System.out.println(millisUntilFinished);
                if (newtime.equals("0:0:0")) {
                    tvWidget.setText("00:00:00");
                } else if ((String.valueOf(min).length() == 1) && (String.valueOf(seconds).length() == 1) && (String.valueOf(milli_seconds).length() == 1)) {
                    tvWidget.setText("0" + min + ":0" + seconds + ":0" + milli_seconds);
                } else if ((String.valueOf(min).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    tvWidget.setText("0" + min + ":0" + seconds + ":" + milli_seconds);
                } else if ((String.valueOf(min).length() == 1) && (String.valueOf(milli_seconds).length() == 1)) {
                    tvWidget.setText("0" + min + ":" + seconds + ":0" + milli_seconds);
                } else if ((String.valueOf(seconds).length() == 1) && (String.valueOf(milli_seconds).length() == 1)) {
                    tvWidget.setText(min + ":0" + seconds + ":0" + milli_seconds);
                } else if (String.valueOf(min).length() == 1) {
                    tvWidget.setText("0" + min + ":" + seconds + ":" + milli_seconds);
                } else if (String.valueOf(seconds).length() == 1) {
                    tvWidget.setText(min + ":0" + seconds + ":" + milli_seconds);
                } else if (String.valueOf(milli_seconds).length() == 1) {
                    tvWidget.setText(min + ":" + seconds + ":0" + milli_seconds);
                } else {
                    tvWidget.setText(min + ":" + seconds + ":" + milli_seconds);
                }

            }

            @Override
            public void onFinish() {
                tvWidget.setText("00:00:00");
                tvWidget.setBackgroundResource(R.drawable.end_circle);
            }
        };
        countDownTimer.start();

    }

    private int time_to_seconds(String timeInterval) {
        String[] array = timeInterval.split(":");
        int result = 0;

        if(array[0].substring(0,1) == "0"){
            result += Integer.parseInt(array[0].substring(1)) * 60;
        }else{
            result += Integer.parseInt(array[0]) * 60;
        }

        if(array[1].substring(0,1) == "0"){
            result += Integer.parseInt(array[1].substring(1));
        }else{
            result += Integer.parseInt(array[1]);
        }

        if(array[2].substring(0,1)=="0"){
            result += Integer.parseInt(array[2].substring(1))/100;
        }else{
            result += Integer.parseInt(array[2])/100;
        }


        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatingView!=null)
        {
            windowManager.removeView(mFloatingView);
        }

        if(imageClose!=null)
        {
            windowManager.removeView(imageClose);
        }

    }
}
