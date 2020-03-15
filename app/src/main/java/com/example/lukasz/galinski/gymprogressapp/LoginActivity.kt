package com.example.lukasz.galinski.gymprogressapp

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_screen.*

private const val USERNAME_LABEL = "username"
private lateinit var square: ImageView
class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        firebaseAuth = FirebaseAuth.getInstance()
        square = findViewById(R.id.logo_imgView)
        userLoggedCheck()
        rotate()
        fadeIn()
        login_btn.setOnClickListener{
            /*
            val userName = username_editTxt.text.toString()
            val userPassword = password_editTxt.text.toString()
            if (userName.isNotBlank() && userPassword.isNotBlank()){
                login(firebaseAuth, userName, userPassword)
            }
            else{
                Toast.makeText(baseContext, resources.getText(R.string.fill_gaps), Toast.LENGTH_SHORT).show()
            }
            */
            finish()
            startActivity(Intent(this, MainMenu::class.java))
        }
        createAccount.setOnClickListener {
            finish()
            startActivity(Intent(this, CreateAccount::class.java))
        }
    }

    private fun login(firebaseAuth: FirebaseAuth, userEmail : String, userPassword: String) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?){
        if (user != null){
            val intent = Intent(applicationContext, MainMenu::class.java)
            intent.putExtra(USERNAME_LABEL, user.email.toString())
            startActivity(intent)
        }
    }

    private fun userLoggedCheck(){
        val user = firebaseAuth.currentUser
        updateUI(user)
    }

    private fun rotate(){
        square.alpha = 0f
        square.visibility = View.VISIBLE
        square.scaleX = 0.1f
        square.scaleY = 0.1f
        square.animate().alpha(1f).rotation(1080f).scaleX(1.5f).scaleY(1.5f).setDuration(4000).setListener(null)
    }

    private fun fadeIn(){
        username_editTxt.alpha = 0f
        password_editTxt.alpha = 0f
        login_btn.alpha = 0f
        createAccount.alpha = 0f
        username_editTxt.animate().alpha(1f).setStartDelay(4500).setDuration(1500).setListener(null)
        password_editTxt.animate().alpha(1f).setStartDelay(4500).setDuration(1500).setListener(null)
        login_btn.animate().alpha(1f).setStartDelay(4500).setDuration(1500).setListener(null)
        createAccount.animate().alpha(1f).setStartDelay(4500).setDuration(1500).setListener(null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        println("Nacisnieto")
        return super.onTouchEvent(event)
    }
}
