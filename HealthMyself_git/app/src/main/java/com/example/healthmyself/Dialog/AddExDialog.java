package com.example.healthmyself.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.healthmyself.CustomClass.DateUtil;
import com.example.healthmyself.CustomClass.PostCalendar;
import com.example.healthmyself.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddExDialog extends Dialog {
    private Context mContext;
    private DatabaseReference mPostReference;



    private EditText et_text_ex;
    private EditText et_text_time;
    private EditText et_text_weight;

    private String uid = "1235323214";

    private Calendar calendar = Calendar.getInstance();
    String day;
    String EX;
    String TIME;
    String WEIGHT;
    String gender = "";
    String sort = "id";

    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        et_text_ex = findViewById(R.id.put_txt_ex);
        et_text_time = findViewById(R.id.put_txt_time);
        et_text_weight = findViewById(R.id.put_txt_weight);

        Button saveButton = findViewById(R.id.btnSave);
        Button cancelButton = findViewById(R.id.btnCancle);

        // 버튼 리스너 설정
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시
                // ...코드..
                EX = et_text_ex.getText().toString();
                TIME = et_text_time.getText().toString();
                WEIGHT = et_text_weight.getText().toString();
                postFirebaseDatabase(true);
                Toast.makeText(mContext,"test", Toast.LENGTH_SHORT).show();
                // Custom Dialog 종료
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '취소' 버튼 클릭시
                // ...코드..
                // Custom Dialog 종료
                dismiss();
            }
        });

    }
    public AddExDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }
    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.CALENDAR_DAY_FORMAT);
        if(add){
            PostCalendar post = new PostCalendar(EX, TIME, WEIGHT);
            postValues = post.toMap();
        }
        childUpdates.put("/"+uid + "/calandar/" + day + "/" , postValues);
        mPostReference.updateChildren(childUpdates);
    }


    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

    public void setUID(String uid){
        this.uid = uid;
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