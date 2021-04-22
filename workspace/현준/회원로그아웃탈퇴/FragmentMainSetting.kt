package com.example.healthmyself.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.healthmyself.AccountDeleteDialog
import com.example.healthmyself.Activity.LoginActivity
import com.example.healthmyself.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_main_setting.*

class FragmentMainSetting : Fragment() {
    var uid : String? = "";
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
        val btn_account_delete = v.findViewById<TextView>(R.id.btn_account_delete)
        btn_logout.setOnClickListener{ signoutAccount(v) }
        btn_account_delete.setOnClickListener{ showDeleteDialog() }
        getUID();
        text_uid.setText("사용자 아이디 : "+uid)

        return v
    }

    private fun signoutAccount(v : View?){
        AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    moveToMainActivity()
                    Toast.makeText(requireContext(), "로그아웃 하셨습니다.", Toast.LENGTH_SHORT).show()
                }
    }
    private fun deleteAccount(){
        AuthUI.getInstance()
                .delete(requireContext())
                .addOnCompleteListener {
                    moveToMainActivity()
                    Toast.makeText(requireContext(), "탈퇴 하셨습니다.", Toast.LENGTH_SHORT).show()
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
        }.show(requireFragmentManager(),"")
    }
    private fun moveToMainActivity(){
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }
    private fun getUID()
    {
        val user : FirebaseUser = FirebaseAuth.getInstance().currentUser;
        Log.d("e", "getUID: "+ user.uid)
        uid = user.uid.toString();
    }
}