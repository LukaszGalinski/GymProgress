package com.example.lukasz.galinski.gymprogressapp.loginfeatures

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.mainmenu.MainMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.account_login_layout.*

private const val USERNAME_LABEL = "username"
private const val ROTATION_DURATION = 4000L
private const val ROTATION = 1080f
private const val SQUARE_SCALE = 1.5f
private const val AlPHA_DELAY = 4500L
private const val START_ALPHA = 0f
private const val END_ALPHA = 1f
private const val ALPHA_DURATION = 1500L
private const val ANIMATION_ALPHA = "alpha"
private val animationsArray: MutableList<ObjectAnimator> = ArrayList()
private lateinit var animatedElementsArray: Array<Any>
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_login_layout)
        animatedElementsArray = arrayOf(app_name, username_editTxt, password_editTxt, createAccount, login_btn)
        val firebaseAuth = FirebaseAuth.getInstance()
        userLoggedCheck()
        rotate()
        fadeIn(animatedElementsArray)
        login_btn.setOnClickListener {
            val userName = username_editTxt.text.toString()
            val userPassword = password_editTxt.text.toString()
            if (userName.isNotBlank() && userPassword.isNotBlank()){
                login(firebaseAuth, userName, userPassword)
            }
            else{
                Toast.makeText(baseContext, resources.getText(R.string.fill_gaps), Toast.LENGTH_SHORT).show()
            }
        }

        createAccount.setOnClickListener {
            finish()
            startActivity(Intent(this, CreateAccount::class.java))
        }
    }

    private fun login(firebaseAuth: FirebaseAuth, userEmail: String, userPassword: String) {
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

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            finish()
            val intent = Intent(applicationContext, MainMenu::class.java)
            intent.putExtra(USERNAME_LABEL, user.email.toString())
            startActivity(intent)
        }
    }

    private fun userLoggedCheck() {
        val user = FirebaseAuth.getInstance().currentUser
        updateUI(user)
    }

    private fun rotate() {
        val square = findViewById<ImageView>(R.id.logo_imgView)
        square.animate().alpha(END_ALPHA).rotation(ROTATION).scaleX(SQUARE_SCALE)
            .scaleY(SQUARE_SCALE).duration = ROTATION_DURATION
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun fadeIn(elementsArray: Array<Any>) {
        for (i in elementsArray.indices) {
            val anim = ObjectAnimator.ofFloat(elementsArray[i], ANIMATION_ALPHA, START_ALPHA, END_ALPHA)
            anim.duration = ALPHA_DURATION
            anim.startDelay = AlPHA_DELAY
            anim.start()
            animationsArray.add(anim)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        animationsFinish()
        return super.onTouchEvent(event)
    }

    private fun animationsFinish() {
        for (i in animationsArray.indices) {
            animationsArray[i].end()
        }
    }
}
