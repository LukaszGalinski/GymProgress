package com.example.lukasz.galinski.gymprogressapp.loginfeatures

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lukasz.galinski.gymprogressapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.account_create_layout.*

class CreateAccount: AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_create_layout)
        firebaseAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            val userLogin = username_editTxt.text.toString()
            val userEmail = user_email.text.toString()
            val userPassword = password_editTxt.text.toString()
            val repeatPassword = reppassword_editTxt.text.toString()

            if (loginCheck(this, userLogin) && emailCheck(this, userEmail) && passwordCheck(this, userPassword)
                && passwordRepeatCheck(this, userPassword, repeatPassword)) {
                firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, resources.getText(R.string.user_added), Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else {
                            Toast.makeText(
                                baseContext,
                                resources.getText(R.string.user_add_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun updateUI() {
        finish()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }
}