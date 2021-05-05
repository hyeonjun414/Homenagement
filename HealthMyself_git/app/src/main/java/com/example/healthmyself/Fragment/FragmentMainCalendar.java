package com.example.healthmyself.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthmyself.Adapter.CalendarAdapter;
import com.example.healthmyself.CustomClass.Keys;
import com.example.healthmyself.Dialog.AddExDialog;
import com.example.healthmyself.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentMainCalendar extends Fragment {

    public int mCenterPosition;
    private long mCurrentTime;

    public ArrayList<Object> mCalendarList = new ArrayList<>();
    //public ArrayList<DayObject> mCalendarList = new ArrayList<>();

    public FloatingActionButton btn_Add;
    public TextView textView;
    public RecyclerView recyclerView;
    private CalendarAdapter mAdapter;
    private StaggeredGridLayoutManager manager;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_calendar, container, false); //뷰 호출
        btn_Add = (FloatingActionButton)rootView.findViewById(R.id.btn_addex);
        textView = (TextView)rootView.findViewById(R.id.title);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.calendar);
        initDatabase();
        initSet();
        setRecycler();



        return rootView;
    }

    // 뷰의 요소를 초기화화


    // 캘린더와 버튼의 초기화
    public void initSet(){
        initCalendarList();
        initBtn();
    }

    // 캘린더 초기화
    public void initCalendarList() {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

    // 버튼 기능 설정
    public void initBtn()
    {
        btn_Add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AddExDialog dlg = new AddExDialog(requireContext());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                dlg.setUID(user.getUid().toString());
                dlg.show();
            }
        });
    }

    // 오브젝트 리스트인 캘린더 리스트를 설정
    public void setCalendarList(GregorianCalendar cal) {

        //setTitle(cal.getTimeInMillis());

        ArrayList<Object> calendarList = new ArrayList<>();
        //ArrayList<DayObject> calendarList = new ArrayList<>();

        // 뒤로 3달 앞으로 3달까지 기록을 확인할 수 있다.
        for (int i = -1; i < 1; i++) {
            try {
                //
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                if (i == 0) {
                    mCenterPosition = calendarList.size(); // 캘린더의 위치를 현재 달에 위치시켜줌
                }

                calendarList.add(calendar.getTimeInMillis()); // 날짜 타입

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구함.
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

                // EMPTY 생성
                for (int j = 0; j < dayOfWeek; j++) {
                    calendarList.add(Keys.EMPTY); // 비어있는 일자 타입
                }
                for (int j = 1; j <= max; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j)); //일자 타입

                }


                // TODO : 결과값 넣을떄 여기다하면될듯

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mCalendarList = calendarList; // 만들어진 달력을 값에 넣어줌.
    }


    //리사이클러뷰 어댑터 연결
    private void setRecycler() {
        // 캘린더의 뷰가 연결되지 않으면 로그 출력
        if (mCalendarList == null) {
            Log.w("e", "No Query, not initializing RecyclerView");
        }

        //그리드 레이아웃의 설정을 해줌
        manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);

        // 캘린더 오브젝트 리스트에 어댑터를 연결
        mAdapter = new CalendarAdapter(mCalendarList);


        mAdapter.setCalendarList(mCalendarList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            recyclerView.scrollToPosition(mCenterPosition);
        }
    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("calendar");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot messageData : snapshot.getChildren()){
                    String msg2 = messageData.getValue().toString();
                    //mCalendarList.add();
                    //mAdapter.add();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }




    //기본 요소
    public static FragmentMainCalendar newInstance(String param1, String param2) {
        FragmentMainCalendar fragment = new FragmentMainCalendar();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentMainCalendar() {}







}