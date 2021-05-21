package com.example.healthmyself.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.healthmyself.Activity.VideoActivity;
import com.example.healthmyself.CustomClass.DateUtil;
import com.example.healthmyself.CustomClass.GetFirebaseData;
import com.example.healthmyself.Dialog.ShowExDialog;
import com.example.healthmyself.R;
import com.example.healthmyself.models.CalendarView.CalendarHeader;
import com.example.healthmyself.models.CalendarView.Day;
import com.example.healthmyself.models.CalendarView.EmptyDay;
import com.example.healthmyself.models.CalendarView.ViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;
    public CalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }


    public void setCalendarList(List<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) { //뷰타입 나누기
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; //날짜 타입
        } else if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE; // 일자 타입

        }
    }


    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 날짜 타입
        if (viewType == HEADER_TYPE) {

            HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.item_calendar_header, parent, false));

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true); //Span을 하나로 통합하기
            viewHolder.itemView.setLayoutParams(params);

            return viewHolder;

            //비어있는 일자 타입
        } else if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, parent, false));

        }
        // 일자 타입
        else {
            return new DayViewHolder(inflater.inflate(R.layout.item_day, parent, false));

        }

    }

    // 데이터 넣어서 완성시키는것
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        /**날짜 타입 꾸미기*/
        /** EX : 2018년 8월*/
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model에 데이터들어감.
                model.setHeader((Long) item);
            }
            // view에 표시하기
            holder.bind(model);
        }
        /** 비어있는 날짜 타입 꾸미기 */
        /** EX : empty */
        else if (viewType == EMPTY_TYPE) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);

        }
        /** 일자 타입 꾸미기 */
        /** EX : 22 */
        else if (viewType == DAY_TYPE) {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {

                // Model에 Calendar값을 넣어서 몇일인지 데이터 넣기
                model.setCalendar((Calendar) item);

            }
            // Model의 데이터를 View에 표현하기
            holder.bind(model);
        }
    }

    // 개수구하기
    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }

    /** viewHolder */
    private class HeaderViewHolder extends RecyclerView.ViewHolder { //날짜 타입 ViewHolder

        TextView itemHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }


        public void initView(View v){

            itemHeaderTitle = (TextView)v.findViewById(R.id.item_header_title);

        }

        public void bind(ViewModel model){

            // 일자 값 가져오기
            String header = ((CalendarHeader)model).getHeader();

            // header에 표시하기, ex : 2018년 8월
            itemHeaderTitle.setText(header);


        };
    }


    private class EmptyViewHolder extends RecyclerView.ViewHolder { // 비어있는 요일 타입 ViewHolder


        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        public void initView(View v){

        }

        public void bind(ViewModel model){


        };
    }

    // TODO : item_day와 매칭
    private class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder


        TextView itemDay;
        TextView itemDay_txt;
        RelativeLayout item_layout;
        Context ct;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);

        }

        public void initView(View v){

            itemDay = (TextView)v.findViewById(R.id.item_day);
            item_layout = (RelativeLayout)v.findViewById(R.id.item_layout);
            ct = v.getContext();
        }

        public void bind(ViewModel model){

            GetFirebaseData gfd = new GetFirebaseData();

            String day = ((Day)model).getDay();
            int day_of_week = ((Day)model).getDOW();
            String flag = ((Day)model).getText();
            gfd.getFirebaseData(((Day)model).getCal(), item_layout, ct);

            Log.d("day", day);
            Log.d("day_of_week", String.valueOf(day_of_week));

            // 일자 값 View에 보이게하기

            if(day_of_week == 1) {
                itemDay.setText(day);
                itemDay.setTextColor(Color.RED);
            }else if (day_of_week == 7) {
                itemDay.setText(day);
                itemDay.setTextColor(Color.BLUE);
            }else{
                itemDay.setText(day);
                itemDay.setTextColor(Color.BLACK);
            }

            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gfd.getFlag())
                    {
                        Intent i = new Intent(ct, ShowExDialog.class);
                        i.putExtra("ex", gfd.getData().getEx());
                        i.putExtra("time", gfd.getData().getTime());
                        i.putExtra("weight", gfd.getData().getWeight());
                        ct.startActivity(i);
                    }
                }
            });
        };
    }

    /*
    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {get.id, get.name, String.valueOf(get.age), get.gender};

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
    }*/
}
