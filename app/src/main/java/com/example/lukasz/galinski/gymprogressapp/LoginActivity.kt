package com.example.lukasz.galinski.gymprogressapp

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_screen_layout.*

private const val USERNAME_LABEL = "username"
private const val ROTATION_DURATION = 4000L
private const val ROTATION = 1080f
private const val SQUARE_SCALE = 1.5f
private const val AlPHA_DELAY = 4500L
private const val START_ALPHA = 0f
private const val END_ALPHA = 1f
private const val ALPHA_DURATION = 1500L
private const val ANIMATION_ALPHA = "alpha"
private lateinit var square: ImageView
private val animationsArray: MutableList<ObjectAnimator> = ArrayList()
private lateinit var elementsArray: Array<Any>
class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen_layout)
        firebaseAuth = FirebaseAuth.getInstance()
        square = findViewById(R.id.logo_imgView)
        elementsArray =
            arrayOf(app_name, username_editTxt, password_editTxt, createAccount, login_btn)
        userLoggedCheck()
        rotate()
        fadeIn(elementsArray)
        login_btn.setOnClickListener {
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
            val intent = Intent(applicationContext, MainMenu::class.java)
            intent.putExtra(USERNAME_LABEL, user.email.toString())
            startActivity(intent)
        }
    }

    private fun userLoggedCheck() {
        val user = firebaseAuth.currentUser
        updateUI(user)
    }

    private fun rotate() {
        square.animate().alpha(END_ALPHA).rotation(ROTATION).scaleX(SQUARE_SCALE).scaleY(
            SQUARE_SCALE
        ).duration = ROTATION_DURATION
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun fadeIn(elementsArray: Array<Any>) {
        for (i in elementsArray.indices) {
            val anim =
                ObjectAnimator.ofFloat(elementsArray[i], ANIMATION_ALPHA, START_ALPHA, END_ALPHA)
            anim.duration = ALPHA_DURATION
            anim.startDelay = AlPHA_DELAY
            anim.start()
            animationsArray.add(anim)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        endAnimations()
        return super.onTouchEvent(event)
    }

    private fun endAnimations() {
        for (i in animationsArray.indices) {
            animationsArray[i].end()
        }
    }
}
