package com.DevBox.Homenagement.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.DevBox.Homenagement.Adapter.CalendarAdapter;
import com.DevBox.Homenagement.CustomClass.Keys;
import com.DevBox.Homenagement.Dialog.AddExDialog;
import com.DevBox.Homenagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main_calendar, container, false);
        btn_Add = (FloatingActionButton)rootView.findViewById(R.id.btn_addex);
        textView = (TextView)rootView.findViewById(R.id.title);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.calendar);
        initSet();
        setRecycler();

        return rootView;
    }

    // 캘린더와 버튼의 초기화
    public void initSet(){
        initBtn();
        initCalendarList();
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

    // 캘린더 초기화
    public void initCalendarList() {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

    // 오브젝트 리스트인 캘린더 리스트를 설정
    public void setCalendarList(GregorianCalendar cal) {
        ArrayList<Object> calendarList = new ArrayList<>();
        
        for (int i = -2; i < 1; i++) {
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
                for (int j = 0; j < dayOfWeek; j++)
                    calendarList.add(Keys.EMPTY); // 비어있는 일자 타입
                for (int j = 1; j <= max; j++) 
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR),
                                 calendar.get(Calendar.MONTH), j)); //일자 타입
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
        manager = new StaggeredGridLayoutManager(7,
                      StaggeredGridLayoutManager.VERTICAL);

        // 캘린더 오브젝트 리스트에 어댑터를 연결
        mAdapter = new CalendarAdapter(mCalendarList);


        mAdapter.setCalendarList(mCalendarList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            recyclerView.scrollToPosition(mCenterPosition);
        }
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