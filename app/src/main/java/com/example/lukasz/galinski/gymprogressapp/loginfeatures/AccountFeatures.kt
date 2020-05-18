package com.example.lukasz.galinski.gymprogressapp.loginfeatures

import android.content.Context
import android.widget.Toast
import com.example.lukasz.galinski.gymprogressapp.R
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

private const val LOGIN_LENGTH = 7
private const val PASSWORD_LENGTH = 7
private const val DIGITAL_PATTERN = "\\d+"
private const val userEmailTrimFormat = "[^\\w\\s]"
private const val UPPERCASE_LETTER_PATTERN = "[A-Z]+"
private val digitalMatcher = Pattern.compile(DIGITAL_PATTERN)
private val uppercaseMatcher = Pattern.compile(UPPERCASE_LETTER_PATTERN)

fun getCurrentUser(): String{
    val firebaseAuth = FirebaseAuth.getInstance().currentUser?.email.toString()
    return firebaseAuth.replace(userEmailTrimFormat.toRegex(), "")
}

fun loginCheck(context: Context, userLogin: String): Boolean {
    return if (userLogin.length < LOGIN_LENGTH) true
    else {
        Toast.makeText(
            context, context.resources.getText(R.string.login_rules, PASSWORD_LENGTH.toString()),
            Toast.LENGTH_SHORT
        ).show()
        false
    }
}

fun emailCheck(context: Context, userEmail: String): Boolean {
    return if (android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) true
    else {
        Toast.makeText(context, context.resources.getText(R.string.email_error), Toast.LENGTH_SHORT).show()
        false
    }
}

fun passwordCheck(context: Context, password: String): Boolean {
    val digitMatcher = digitalMatcher.matcher(password)
    val uppercaseMatcher = uppercaseMatcher.matcher(password)
    return if (password.length > PASSWORD_LENGTH && digitMatcher.find() && uppercaseMatcher.find()) true
    else {
        Toast.makeText(context, context.resources.getText(R.string.password_rules, PASSWORD_LENGTH.toString()), Toast.LENGTH_SHORT).show()
        false
    }
}

fun passwordRepeatCheck(context: Context, password: String, passwordRepeated: String): Boolean {
    return if (password == passwordRepeated) true
    else {
        Toast.makeText(context, context.resources.getText(R.string.password_difference), Toast.LENGTH_SHORT).show()
        false
    }
}