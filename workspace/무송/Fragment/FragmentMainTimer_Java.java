package com.example.healthmyself.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthmyself.Activity.PopUpActivity;
import com.example.healthmyself.R;
import com.example.healthmyself.Service.WidgetService;
import com.example.healthmyself.Service.WidgetService.MyBinder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentMainTimer_Java extends Fragment {

    public static Context context_timer;
    String time = "00:00:00";
    Button buttonAddWidget;
    TextView tv_time;
    CountDownTimer countDownTimer;
    int endTime = 250;
    int flag = 0;
    ViewGroup rootView;
    WidgetService ws;
    boolean isService = false;
    FloatingActionButton play ;
    FloatingActionButton pause ;
    FloatingActionButton reset ;

    ServiceConnection conn = new ServiceConnection() {
        //service와 통신을 위한 연결체? 선언
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {

            MyBinder mb = (MyBinder) service;
            ws = mb.getService();
            isService = true;
            //연결이 된다면 
        }
        public void onServiceDisconnected(ComponentName name) {
            isService = false;
            buttonAddWidget.setEnabled(true);
            buttonAddWidget.setText("Run Mini Timer!");
            //연결이 끊기면
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_timer, container, false);
        context_timer = getContext();
        buttonAddWidget = (Button)rootView.findViewById(R.id.button_widget);
        tv_time = (TextView)rootView.findViewById(R.id.tv_time);
        play = (FloatingActionButton)rootView.findViewById(R.id.btn_play);
        pause = (FloatingActionButton)rootView.findViewById(R.id.btn_pause);
        reset = (FloatingActionButton)rootView.findViewById(R.id.btn_reset);

        buttonAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Settings.canDrawOverlays((requireContext())))
                {
                    getPermission();
                }else{
                    Log.d("e", "onClick: widgetbutton");
                    Intent intent = new Intent(requireContext(), WidgetService.class);
                    intent.putExtra("name" , time);
                    //타이머를 생성해주는 서비스 호출 onstart 용
                    requireContext().startService(intent);
                    //통신을 위한 서비스 호출 onbind 용
                    requireContext().bindService(intent, conn, Context.BIND_ABOVE_CLIENT);
                    buttonAddWidget.setText("Already running");
                    buttonAddWidget.setEnabled(false);
                }
            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), PopUpActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((flag==0)&&(!tv_time.getText().toString().equals("00:00:00"))){
                    fn_countdown();
                    flag=1;
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                flag=0;
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                tv_time.setText(time);
                flag=0;
            }
        });


        return rootView;
    }
    public void getPermission()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(requireContext()))
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + requireContext().getPackageName()));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(!Settings.canDrawOverlays(requireContext()))
            {
                Toast.makeText(requireContext(), "Permission denied ", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == 2){
            //결과 값 처리 하는 부분 PopUp
            String min = data.getStringExtra("min");
            String sec = data.getStringExtra("sec");
            int check_num = -1;

            if(sec.equals("00")||sec.equals("0")||sec.equals("")){
                sec = "00";
            }else if(Integer.parseInt(sec) > 59){
                check_num = Integer.parseInt(sec) - 60;
                sec = Integer.toString(check_num);
            }

            if(sec.length() == 1){
                sec = "0" + sec;
            }

            if(min.equals("00")||min.equals("0")||min.equals("")){
                min = "00";
            }

            if(check_num > -1){
                min = Integer.toString(Integer.parseInt(min) + 1);
            }

            if(min.length()==1){
                min = "0" + min;
            }

            time = min + ":" + sec + ":00";
            tv_time.setText(time);
        }
    }

    private void fn_countdown() {
        String timeInterval = tv_time.getText().toString();
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
                    tv_time.setText("00:00:00");
                } else if ((String.valueOf(min).length() == 1) && (String.valueOf(seconds).length() == 1) && (String.valueOf(milli_seconds).length() == 1)) {
                    tv_time.setText("0" + min + ":0" + seconds + ":0" + milli_seconds);
                } else if ((String.valueOf(min).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    tv_time.setText("0" + min + ":0" + seconds + ":" + milli_seconds);
                } else if ((String.valueOf(min).length() == 1) && (String.valueOf(milli_seconds).length() == 1)) {
                    tv_time.setText("0" + min + ":" + seconds + ":0" + milli_seconds);
                } else if ((String.valueOf(seconds).length() == 1) && (String.valueOf(milli_seconds).length() == 1)) {
                    tv_time.setText(min + ":0" + seconds + ":0" + milli_seconds);
                } else if (String.valueOf(min).length() == 1) {
                    tv_time.setText("0" + min + ":" + seconds + ":" + milli_seconds);
                } else if (String.valueOf(seconds).length() == 1) {
                    tv_time.setText(min + ":0" + seconds + ":" + milli_seconds);
                } else if (String.valueOf(milli_seconds).length() == 1) {
                    tv_time.setText(min + ":" + seconds + ":0" + milli_seconds);
                } else {
                    tv_time.setText(min + ":" + seconds + ":" + milli_seconds);
                }

            }
            /////////////////////////////////////////////////////
            @Override
            public void onFinish() {
                flag = 0;
                tv_time.setText("00:00:00");
                //타이머 종료시
            }
        };
        countDownTimer.start();
/////////////////////////////////////////////////////
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
}