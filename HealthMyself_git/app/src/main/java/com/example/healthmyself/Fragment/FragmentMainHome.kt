package com.example.healthmyself.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthmyself.Activity.MainActivity
import com.example.healthmyself.R
import org.w3c.dom.Text
import java.util.*
import kotlin.math.log


class FragmentMainHome : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var year = 0
    var month = 0
    var day = 0
    var time_x : Long = 0
    var time_y : Long = 0
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_home, null)
        val youtube_fragment = view.findViewById<TextView>(R.id.youtube)
        val timer_fragment = view.findViewById<TextView>(R.id.timer)
        val set_fragment = view.findViewById<TextView>(R.id.set)
        val cal_fragment = view.findViewById<TextView>(R.id.cal)
        val tv_d_day = view.findViewById<TextView>(R.id.d_day_text)
        val progress = view.findViewById<ProgressBar>(R.id.d_day)

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
                showDatePicker()

            }
        })
        // Inflate the layout for this fragment


        return view
    }
    fun showDatePicker() {
        val cal = Calendar.getInstance()
        time_x = cal.timeInMillis
        getActivity()?.let {
            DatePickerDialog(it, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
                cal.set(y, m, d)
                time_y = cal.timeInMillis
                Toast.makeText(getActivity(), ((time_y - time_x)/(24*60*60*1000)).toString(), Toast.LENGTH_SHORT).show()
            }, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE)).show()
        }
    }


}