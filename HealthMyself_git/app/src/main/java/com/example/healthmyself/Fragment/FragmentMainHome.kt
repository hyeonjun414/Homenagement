package com.example.healthmyself.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.healthmyself.Activity.MainActivity
import com.example.healthmyself.R
import org.w3c.dom.Text


class FragmentMainHome : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_home, null)

        val youtube_fragment = view.findViewById<TextView>(R.id.youtube)
        val timer_fragment = view.findViewById<TextView>(R.id.timer)
        val set_fragment = view.findViewById<TextView>(R.id.set)
        val food_fragment = view.findViewById<TextView>(R.id.food)
        val cal_fragment = view.findViewById<TextView>(R.id.cal)
        val alram_fragment = view.findViewById<TextView>(R.id.alram)
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
        food_fragment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainActivity?)!!.selectFragment(1)
            }
        })
        cal_fragment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainActivity?)!!.selectFragment(1)
            }
        })
        alram_fragment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as MainActivity?)!!.selectFragment(4)
            }
        })
        // Inflate the layout for this fragment
        return view
    }

}