/*
package com.example.healthmyself.Fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthmyself.Activity.PopUpActivity
import com.example.healthmyself.R
import com.example.healthmyself.Service.WidgetService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.reflect.Reflection.getPackageName

class FragmentMainTimer : Fragment() {

    var time: String = "00:00:00"
    public var tv_time : TextView? = null
    var countDownTimer: CountDownTimer? = null
    var endTime = 250
    var flag = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_main_setting, null)
        var time = "00:00:00"
        var buttonAddWidget = v.findViewById<Button>(R.id.button_widget)
        var tv_time = v.findViewById<TextView>(R.id.tv_time)
        var endTime = 250
        var flag = 0

        var play = v.findViewById<FloatingActionButton>(R.id.btn_play)
        var pause = v.findViewById<FloatingActionButton>(R.id.btn_pause)
        var reset = v.findViewById<FloatingActionButton>(R.id.btn_reset)



        getPermission()
        buttonAddWidget.setOnClickListener(View.OnClickListener {
            if (!Settings.canDrawOverlays(requireContext())) {
                getPermission()
            } else {
                val intent = Intent(requireContext(), WidgetService::class.java)
                intent.putExtra("name", time)
                requireContext().startService(intent)
                buttonAddWidget.setText("Already running")
                buttonAddWidget.setEnabled(false)
            }
        })

        tv_time.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireContext(), PopUpActivity::class.java)
            startActivityForResult(intent, 2)
        })

        play.setOnClickListener(View.OnClickListener {
            if (flag == 0) {
                fn_countdown()
                flag = 1
            }
        })
        pause.setOnClickListener(View.OnClickListener {
            countDownTimer.cancel()
            flag = 0
        })
        reset.setOnClickListener(View.OnClickListener {
            countDownTimer.cancel()
            tv_time.setText(time)
            flag = 0
        })


        return v
    }
    fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireActivity().getPackageName())
            )
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (!Settings.canDrawOverlays(requireContext())) {
                Toast.makeText(requireContext(), "Permission denied ", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 2) {
            //결과 값 처리 하는 부분 PopUp
            var min = data!!.getStringExtra("min")
            var sec = data.getStringExtra("sec")
            var check_num = -1
            if (sec == "00" || sec == "0" || sec == "") {
                sec = "00"
            } else if (sec!!.toInt() > 59) {
                check_num = sec.toInt() - 60
                sec = Integer.toString(check_num)
            }
            if (sec!!.length == 1) {
                sec = "0$sec"
            }
            if (min == "00" || min == "0" || min == "") {
                min = "00"
            }
            if (check_num > -1) {
                min = Integer.toString(min!!.toInt() + 1)
            }
            if (min!!.length == 1) {
                min = "0$min"
            }
            time = "$min:$sec:00"
            tv_time.setText(time)
        }
    }

    private fun fn_countdown(tv_time : TextView?, countDownTimer : CountDownTimer?) {
        val timeInterval: String = tv_time?.getText().toString()
        endTime = time_to_seconds(timeInterval)
        countDownTimerobject : CountDownTimer(endTime * 1000, 1) {
            override fun onTick(millisUntilFinished: Long) {
                val milli_seconds = (millisUntilFinished % 100).toInt()
                val seconds = (millisUntilFinished / 1000).toInt() % 60
                val min = (millisUntilFinished / (1000 * 60) % 60).toInt()
                val newtime = "$min:$seconds:$milli_seconds"
                println(millisUntilFinished)
                if (newtime == "0:0:0") {
                    tv_time?.setText("00:00:00")
                } else if (min.toString().length == 1 && seconds.toString().length == 1 && milli_seconds.toString().length == 1) {
                    tv_time?.setText("0$min:0$seconds:0$milli_seconds")
                } else if (min.toString().length == 1 && seconds.toString().length == 1) {
                    tv_time?.setText("0$min:0$seconds:$milli_seconds")
                } else if (min.toString().length == 1 && milli_seconds.toString().length == 1) {
                    tv_time?.setText("0$min:$seconds:0$milli_seconds")
                } else if (seconds.toString().length == 1 && milli_seconds.toString().length == 1) {
                    tv_time?.setText("$min:0$seconds:0$milli_seconds")
                } else if (min.toString().length == 1) {
                    tv_time?.setText("0$min:$seconds:$milli_seconds")
                } else if (seconds.toString().length == 1) {
                    tv_time?.setText("$min:0$seconds:$milli_seconds")
                } else if (milli_seconds.toString().length == 1) {
                    tv_time?.setText("$min:$seconds:0$milli_seconds")
                } else {
                    tv_time?.setText("$min:$seconds:$milli_seconds")
                }
            }

            /////////////////////////////////////////////////////
            override fun onFinish() {
                tv_time?.setText("00:00:00")
                //타이머 종료시
            }
        }.also { countDownTimer = it }
        countDownTimer?.start()
/////////////////////////////////////////////////////
    }

    private fun time_to_seconds(timeInterval: String): Int {
        val array = timeInterval.split(":".toRegex()).toTypedArray()
        var result = 0
        result += if (array[0].substring(0, 1) === "0") {
            array[0].substring(1).toInt() * 60
        } else {
            array[0].toInt() * 60
        }
        result += if (array[1].substring(0, 1) === "0") {
            array[1].substring(1).toInt()
        } else {
            array[1].toInt()
        }
        result += if (array[2].substring(0, 1) === "0") {
            array[2].substring(1).toInt() / 100
        } else {
            array[2].toInt() / 100
        }
        return result
    }
}*/
