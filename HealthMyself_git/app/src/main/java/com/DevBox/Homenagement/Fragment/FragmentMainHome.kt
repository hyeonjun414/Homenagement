package com.DevBox.Homenagement.Fragment

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.DevBox.Homenagement.Activity.MainActivity
import com.DevBox.Homenagement.R
import java.util.*


class FragmentMainHome : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var time_x : Long = 0
    var time_y : Long = 0
    var time_x_pref : Long = 0
    var time_y_pref: Long = 0
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val pref = requireActivity().getPreferences(0)
        val editor = pref.edit()
        val view = inflater.inflate(R.layout.fragment_main_home, null)
        val youtube_fragment = view.findViewById<TextView>(R.id.youtube)
        val timer_fragment = view.findViewById<TextView>(R.id.timer)
        val set_fragment = view.findViewById<TextView>(R.id.set)
        val cal_fragment = view.findViewById<TextView>(R.id.cal)
        val tv_d_day = view.findViewById<TextView>(R.id.d_day_text)
        val progress = view.findViewById<ProgressBar>(R.id.d_day)

        if(pref.getLong("d-day",time_x_pref)==0.toLong()){
            tv_d_day.setText("D-??")
        }else{
            setting_d_day(pref, tv_d_day, progress)
        }

        youtube_fragment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainActivity?)!!.selectFragment(3)
            }
        })
        timer_fragment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainActivity?)!!.selectFragment(0)
            }
        })
        set_fragment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainActivity?)!!.selectFragment(4)
            }
        })
        cal_fragment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainActivity?)!!.selectFragment(1)
            }
        })

        tv_d_day.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showDatePicker(pref, tv_d_day, progress)

            }
        })
        // Inflate the layout for this fragment


        return view
    }
    fun showDatePicker(pref :SharedPreferences, tv_d_day : TextView, progress :ProgressBar) {
        val cal = Calendar.getInstance()
        var year : Int
        var month : Int
        var day : Int
        val editor = pref.edit()
        getActivity()?.let {
            DatePickerDialog(it,  R.style.DialogTheme ,DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
                editor.putLong("setting_day", cal.timeInMillis).apply()
                cal.set(y, m, d)
                time_x = cal.timeInMillis
                editor.putLong("d-day", time_x).apply()
                setting_d_day(pref, tv_d_day, progress)
            }, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE)).show()
        }

    }

    fun setting_d_day(pref : SharedPreferences, tv_d_day: TextView, progress : ProgressBar){
        val cal = Calendar.getInstance()
        time_x_pref = pref.getLong("d-day",time_x_pref)
        time_y_pref = pref.getLong("setting_day", time_y_pref)
        val d_day = Math.round((time_x_pref - cal.timeInMillis)/(24*60*60*1000).toFloat())
        val past_day = Math.round((cal.timeInMillis - time_y_pref )/(24*60*60*1000).toFloat())

        tv_d_day.setText("D-" + (d_day).toString())

        if(tv_d_day.text.equals("D-0")){
            tv_d_day.setText("D-DAY!!!")
            progress.setProgress(100)
        }else {
            progress.setProgress((past_day.toDouble()/d_day.toDouble()*100).toInt())
        }
    }

}