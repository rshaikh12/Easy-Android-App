package com.example.easy.ui.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.easy.R
import com.example.easy.data.model.LoggedInUser
import com.example.easy.ui.mainMenu.activity.MainMenuActivity
import com.google.gson.Gson

/**
 * @author Marius Funk
 *
 * Main Login
 *
 * Based on boilerplate code, but extensively reworked
 *
 */

class LoginActivity : AppCompatActivity() {


    private lateinit var loginViewModel: LoginViewModel

    private val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.username)
        val firstName = findViewById<EditText>(R.id.firstName)
        val secondName = findViewById<EditText>(R.id.secondName)
        val password = findViewById<EditText>(R.id.password)
        val password2 = findViewById<EditText>(R.id.password2)

        val loading = findViewById<ProgressBar>(R.id.loading)

        val login = findViewById<Button>(R.id.login)
        val register = findViewById<Button>(R.id.register)


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        /**
         * Obverve the login state to update UI
         */

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer


            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid || loginState.isRegisterValid || (loginState.password2Error != null)
                    || (loginState.firstNameError != null)|| (loginState.secondNameError != null)

            //disable register button unless all entries are valid
            register.isEnabled = loginState.isRegisterValid


            /**
             * checks if all fields are entered
             */
            if (loginState.usernameError != null) {
                email.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
            if (loginState.password2Error != null) {
                password2.error = getString(loginState.password2Error)
            }
            if (loginState.firstNameError != null) {
                firstName.error = getString(loginState.firstNameError)
            }
            if (loginState.secondNameError != null) {
                secondName.error = getString(loginState.secondNameError)
            }
        })
        /**
         * Check if login is wrong. Will display pop up if wrong
         */
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {

                openMainMenu()
            }
            setResult(Activity.RESULT_OK)

            finish()
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                    email.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                firstName.text.toString(),
                secondName.text.toString()
            )
        }

        password2.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                firstName.text.toString(),
                secondName.text.toString()
            )
        }
        firstName.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                firstName.text.toString(),
                secondName.text.toString()
            )
        }
        secondName.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString(),
                password2.text.toString(),
                firstName.text.toString(),
                secondName.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    password2.text.toString(),
                    firstName.text.toString(),
                    secondName.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                                email.text.toString(),
                                password.text.toString(),
                             this@LoginActivity
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(email.text.toString(), password.text.toString(), this@LoginActivity)
            }

            register.setOnClickListener{
                loading.visibility = View.VISIBLE
                loginViewModel.register(email.text.toString(), firstName.text.toString(), secondName.text.toString(), password.text.toString(), password2.text.toString(), applicationContext)
            }
        }
    }

    /**
     * Method to open the main menu
     * Will input the user as json intent
     */
    private fun openMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        var gson = Gson()

        intent.putExtra("USER", gson.toJson(loginViewModel.getUser()))
        startActivity(intent)
    }

    /**
     * Shows failed login
     */
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}