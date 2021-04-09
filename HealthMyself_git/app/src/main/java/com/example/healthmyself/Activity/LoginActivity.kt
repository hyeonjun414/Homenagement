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
        Log.d("test", "Name : " + user?.email)

        if(user == null) showLoginWindow()
        else moveToMain()
    }

    private fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun showLoginWindow() {
        // Choose authentication providers
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.GreenTheme)
                        .setLogo(R.drawable.applogo)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                // 로그인 성공
                val user = FirebaseAuth.getInstance().currentUser
                moveToMain()
                // ...
            } else {
                // 로그인 실패할 경우
                Toast.makeText(this, "로그인 실패, 로그인을 다시 실행하세요.", Toast.LENGTH_LONG).show()
            }
        }
    }
}