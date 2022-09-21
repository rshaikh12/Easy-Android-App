package com.example.easy.ui.mainMenu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.easy.R
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast

class ProfileActivity : AppCompatActivity()  {

    lateinit var firstName:EditText
    lateinit var secondName:EditText
    lateinit var displayName:EditText
    lateinit var userEmail: EditText
    lateinit var userLatitude:EditText
    lateinit var userLongitude:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        viewInitializations()
    }
    fun viewInitializations() {

        firstName = findViewById(R.id.firstName)
        secondName = findViewById(R.id.secondName)
        displayName = findViewById(R.id.displayName)
        userEmail = findViewById(R.id.userEmail)
        userLatitude = findViewById(R.id.userLatitude)
        userLongitude = findViewById(R.id.userLongitude)

        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Check if the input in form is valid
    fun validateInput(): Boolean {
        if (firstName.text.toString().equals("")) {
            firstName.setError("Please enter your first name")
            return false
        }
        if (secondName.text.toString().equals("")) {
            secondName.setError("Please enter your second name")
            return false
        }
        if (displayName.text.toString().equals("")) {
            displayName.setError("Please enter display name")
            return false
        }
        if (userEmail.text.toString().equals("")) {
            userEmail.setError("Please enter email")
            return false
        }
        if (userLatitude.text.toString().equals("")) {
            userLatitude.setError("Please enter your location latitude")
            return false
        }
        if (userLongitude.text.toString().equals("")) {
            userLongitude.setError("Please enter your location longitude")
            return false
        }
        // check the proper email format
        if (!isEmailValid(userEmail.text.toString())) {
            userEmail.setError("Please enter valid email")
            return false
        }
        return true
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    // Hooking Click event

    fun performEditProfile (view: View) {
        if (validateInput()) {

            // Input is valid, here send data to the server

            val first_Name = firstName.text.toString()
            val second_Name = secondName.text.toString()
            val display_Name = displayName.text.toString()
            val user_Email = userEmail.text.toString()
            val user_Latitude = userLatitude.text.toString()
            val user_Longitude = userLongitude.text.toString()

            Toast.makeText(this,"Profile updated successfully",Toast.LENGTH_SHORT).show()
            // Here is a call of API

        }
    }

}