package com.DevBox.Homenagement.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.DevBox.Homenagement.Activity.YoutubeActivity
import com.DevBox.Homenagement.R

class FragmentMainVideo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_video, null)

        val ex1 = view.findViewById<TextView>(R.id.btn_upperbody)
        val ex2 = view.findViewById<TextView>(R.id.btn_lower_body)
        val ex3 = view.findViewById<TextView>(R.id.btn_aerobic)
        val ex4 = view.findViewById<TextView>(R.id.btn_core)

        ex1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("test", "onClick: ")
                val intent = Intent(context, YoutubeActivity::class.java)
                intent.putExtra("keyword", "상체 홈트")
                startActivity(intent)
            }
        })
        ex2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("test", "onClick: ")
                val intent = Intent(context, YoutubeActivity::class.java)
                intent.putExtra("keyword", "하체 홈트")
                startActivity(intent)
            }
        })
        ex3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("test", "onClick: ")
                val intent = Intent(context, YoutubeActivity::class.java)
                intent.putExtra("keyword", "유산소 홈트")
                startActivity(intent)
            }
        })
        ex4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("test", "onClick: ")
                val intent = Intent(context, YoutubeActivity::class.java)
                intent.putExtra("keyword", "코어 홈트")
                startActivity(intent)
            }
        })
        return view
    }

}