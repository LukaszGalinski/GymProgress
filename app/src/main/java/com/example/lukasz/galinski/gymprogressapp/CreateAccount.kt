package com.example.lukasz.galinski.gymprogressapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.create_account.*
import java.util.regex.Pattern

private const val LOGIN_LENGTH = 7
private const val PASSWORD_LENGTH = 7
private const val DIGITAL_PATTERN = "\\d+"
private const val UPPERCASE_LETTER_PATTERN = "[A-Z]+"
private val digitalMatcher = Pattern.compile(DIGITAL_PATTERN)
private val uppercaseMatcher = Pattern.compile(UPPERCASE_LETTER_PATTERN)

class CreateAccount: AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)
        firebaseAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            val userLogin = username_editTxt.text.toString()
            val userEmail = user_email.text.toString()
            val userPassword = password_editTxt.text.toString()
            val repeatPassword = reppassword_editTxt.text.toString()

            if (loginCheck(userLogin) && emailCheck(userEmail) && passwordCheck(userPassword) && passwordReapeatCheck(
                    userPassword,
                    repeatPassword
                )
            ) {
                firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                resources.getText(R.string.user_added),
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun loginCheck(userLogin: String): Boolean {
        return if (userLogin.length < LOGIN_LENGTH) true
        else {
            Toast.makeText(
                this,
                resources.getText(R.string.login_rules, PASSWORD_LENGTH.toString()),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    private fun emailCheck(userEmail: String): Boolean {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) true
        else {
            Toast.makeText(this, resources.getText(R.string.email_error), Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun passwordCheck(password: String): Boolean {
        val digitMatcher = digitalMatcher.matcher(password)
        val uppercaseMatcher = uppercaseMatcher.matcher(password)
        return if (password.length > PASSWORD_LENGTH || digitMatcher.find() || uppercaseMatcher.find()) true
        else {
            Toast.makeText(
                this,
                resources.getText(R.string.password_rules, PASSWORD_LENGTH.toString()),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    private fun passwordReapeatCheck(password: String, passwordRepeated: String): Boolean {
        return if (password == passwordRepeated) true
        else {
            Toast.makeText(
                baseContext,
                resources.getText(R.string.password_difference),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    private fun updateUI() {
        finish()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }
}