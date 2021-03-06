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

    // ???????????? ????????? ?????????
    public void initSet(){
        initBtn();
        initCalendarList();
    }
    // ?????? ?????? ??????
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

    // ????????? ?????????
    public void initCalendarList() {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

    // ???????????? ???????????? ????????? ???????????? ??????
    public void setCalendarList(GregorianCalendar cal) {
        ArrayList<Object> calendarList = new ArrayList<>();
        
        for (int i = -2; i < 1; i++) {
            try {
                //
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                if (i == 0) {
                    mCenterPosition = calendarList.size(); // ???????????? ????????? ?????? ?????? ???????????????
                }

                calendarList.add(calendar.getTimeInMillis()); // ?????? ??????

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //?????? ?????? ???????????? ?????? -1 ??? ?????? ????????? ??????.
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // ?????? ?????? ????????? ??????

                // EMPTY ??????
                for (int j = 0; j < dayOfWeek; j++)
                    calendarList.add(Keys.EMPTY); // ???????????? ?????? ??????
                for (int j = 1; j <= max; j++) 
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR),
                                 calendar.get(Calendar.MONTH), j)); //?????? ??????
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCalendarList = calendarList; // ???????????? ????????? ?????? ?????????.
    }


    //?????????????????? ????????? ??????
    private void setRecycler() {
        // ???????????? ?????? ???????????? ????????? ?????? ??????
        if (mCalendarList == null) {
            Log.w("e", "No Query, not initializing RecyclerView");
        }

        //????????? ??????????????? ????????? ??????
        manager = new StaggeredGridLayoutManager(7,
                      StaggeredGridLayoutManager.VERTICAL);

        // ????????? ???????????? ???????????? ???????????? ??????
        mAdapter = new CalendarAdapter(mCalendarList);


        mAdapter.setCalendarList(mCalendarList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            recyclerView.scrollToPosition(mCenterPosition);
        }
    }
    //?????? ??????
    public static FragmentMainCalendar newInstance(String param1, String param2) {
        FragmentMainCalendar fragment = new FragmentMainCalendar();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentMainCalendar() {}







}