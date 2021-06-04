package com.example.healthmyself.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.healthmyself.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loding)
        checkPreviousLogin()
    }
    private fun checkPreviousLogin() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null)
            showLoginWindow()
        else
            moveToMain()
    }
    private fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun showLoginWindow() {
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MainTheme)
                        .setLogo(R.drawable.applogo_2)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                moveToMain()
            } else {
                Toast.makeText(this, "로그인 실패, 로그인을 다시 실행하세요.", Toast.LENGTH_LONG).show()
            }
        }
    }
}