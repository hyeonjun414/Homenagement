package com.DevBox.Homenagement.Dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.DevBox.Homenagement.R;


public class ShowExDialog extends Activity {
    private Context mContext;
    String ex="";
    String time="";
    String weight="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_ex_dialog);
        TextView txt_ex = findViewById(R.id.txt_showEx_ex);
        TextView txt_time = findViewById(R.id.txt_showEx_time);
        TextView txt_weight = findViewById(R.id.txt_showEx_weight);
        Button cancelButton = findViewById(R.id.btn_showEx_Cancel);

        ex = getIntent().getStringExtra("ex");
        time = getIntent().getStringExtra("time");
        weight = getIntent().getStringExtra("weight");

        txt_ex.setText(ex);
        txt_time.setText(time);
        txt_weight.setText(weight+" kg");

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '취소' 버튼 클릭시
                // ...코드..
                // Custom Dialog 종료
                finish();
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}