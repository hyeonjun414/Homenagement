package com.DevBox.Homenagement.Fragment

import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.DevBox.Homenagement.Activity.LoginActivity
import com.DevBox.Homenagement.Dialog.AboutDialog
import com.DevBox.Homenagement.Dialog.AccountDeleteDialog
import com.DevBox.Homenagement.Dialog.AlarmDialog
import com.DevBox.Homenagement.R
import com.DevBox.Homenagement.Service.AlarmReceiver
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FragmentMainSetting : Fragment() {
    var uid : String? = ""
    var alarminfo : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_main_setting, null)
        val text_uid = v.findViewById<TextView>(R.id.text_uid)
        val btn_logout = v.findViewById<TextView>(R.id.btn_logout)
        val text_alarminfo = v.findViewById<TextView>(R.id.alarminfo_txt)
        alarminfo = text_alarminfo
        val text_about = v.findViewById<TextView>(R.id.txt_about)
        val btn_account_delete = v.findViewById<TextView>(R.id.btn_account_delete)
        val check_alarm = v.findViewById<CheckBox>(R.id.alarm_check)
        val existalramCheck = v.findViewById<Button>(R.id.existAlarmCheck)
        
        val load = requireContext().getSharedPreferences("pref", 0)
        var existAlarm = load.getBoolean("alarm", false)
        if(existAlarm) check_alarm.toggle()

        check_alarm.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked) moveToSetAlarm()
            else
            {
                Toast.makeText(requireContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show()
                cancelAlarm()
            }
        }
        text_about.setOnClickListener{ moveToAbout() }
        existalramCheck.setOnClickListener{ checkAlarm() }
        btn_logout.setOnClickListener{ signoutAccount(v) }
        btn_account_delete.setOnClickListener{ showDeleteDialog() }
        
        getUID();
        text_uid.setText("???????????????! " + uid + "???")
        updateAlarminfo()

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                updateAlarminfo()
            }
        }
    }

    private fun signoutAccount(v: View?){
        AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    moveToMainActivity()
                    Toast.makeText(requireContext(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
    }
    private fun showDeleteDialog() {
        AccountDeleteDialog().apply {
            addAccountDeleteDialogInterface(object :
                    AccountDeleteDialog.AccountDeleteDialogInterface {
                override fun delete() {
                    deleteAccount()
                }
                override fun cancelDelete() {
                }
            })
        }.show(requireFragmentManager(), "")
    }
    private fun deleteAccount(){
        AuthUI.getInstance()
                .delete(requireContext())
                .addOnCompleteListener {
                    moveToMainActivity()
                    Toast.makeText(requireContext(), "?????? ???????????????.", Toast.LENGTH_SHORT).show()
                }
    }
    
    private fun moveToMainActivity(){
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun moveToSetAlarm(){
        val intent = Intent(requireContext(), AlarmDialog::class.java)
        startActivityForResult(intent, 1)
    }
    private fun moveToAbout(){
        val intent = Intent(requireContext(), AboutDialog::class.java)
        startActivity(intent)
    }

    private fun cancelAlarm(){
        val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if(sender != null){
            am.cancel(sender)
            sender.cancel()
            val save: SharedPreferences = requireContext().getSharedPreferences("pref", 0)
            val edit = save.edit()
            edit.putBoolean("alarm", false)
            val save2: SharedPreferences = requireContext().getSharedPreferences("daily alarm", 0)
            val edit2 = save2.edit()
            edit2.remove("Alarminfo")
            edit.apply()
            edit2.apply()
            updateAlarminfo()
        }
    }
    public fun updateAlarminfo()
    {
        val save: SharedPreferences = requireContext().getSharedPreferences("daily alarm", 0)
        val str = save.getString("Alarminfo", "")
        if(str == "")
            alarminfo?.setText("?????? ????????? ????????????")
        else
            alarminfo?.setText("?????? ????????? "+str.toString()+"?????????")
    }
    private fun checkAlarm()
    {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_NO_CREATE)
        if(sender == null){
            Toast.makeText(requireContext(), "????????? ????????????.", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(requireContext(), "????????? ????????????. ", Toast.LENGTH_SHORT).show()
        }

    }
    private fun getUID()
    {
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser;
        Log.d("e", "getUID: " + user?.displayName)
        uid = user?.displayName.toString();
    }
}