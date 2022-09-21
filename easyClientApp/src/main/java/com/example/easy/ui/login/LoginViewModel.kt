package com.example.easy.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import com.example.easy.data.LoginRepository
import com.example.easy.data.Result

import com.example.easy.R
import com.example.easy.data.model.LoggedInUser
import com.example.easy.rest.SessionManager

/**
 * @author Marius Funk
 */
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun getUser(): LoggedInUser? {
        return loginRepository.user
    }

    /**
     * Manages the login including starting a thread
     */
    fun login(username: String, password: String, context: Context) {
        val runnable = LoginProcessRunnable(username, password, context, loginRepository, _loginResult)
        val loginThread = Thread(runnable)
        loginThread.start()
    }
    /**
     * Manages the registration including starting a thread
     */
    fun register(email: String, firstName: String, secondName: String, password: String, password2: String, context: Context)  {
        val runnable = RegisterProcessRunnable(email, firstName, secondName, password, password2, context, loginRepository, _loginResult)
        val loginThread = Thread(runnable)
        loginThread.start()
    }


    /**
     * Handles all input fields for the login, inputs must be valid
     */
    fun loginDataChanged(username: String, password: String, password2: String, firstName: String, secondName: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)

            if(!arePasswordsEqual(password,password2)){
                _loginForm.value = LoginFormState(password2Error = R.string.invalid_password2)
            } else if(firstName.isEmpty()){
                _loginForm.value = LoginFormState(firstNameError = R.string.invalid_firstName)
            } else if(secondName.isEmpty()){
                _loginForm.value = LoginFormState(secondNameError = R.string.invalid_secondName)
            } else{
                _loginForm.value = LoginFormState(isRegisterValid = true)
            }
        }
    }

    // A simple username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A simple password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    /**
     * Check if password is as intended
     */
    private fun arePasswordsEqual(password: String,password2: String) : Boolean{
        return password == password2
    }


}

/**
 * Process to handle login on a different thread
 */
class LoginProcessRunnable(
    username: String,
    password: String,
    context: Context,
    loginRepository: LoginRepository,
    _loginResult: MutableLiveData<LoginResult>) : Runnable {
    val username = username
    val password = password
    val context = context
    val loginRepository = loginRepository
    val _loginResult = _loginResult
    val sessionManager = SessionManager(context)

    override fun run() {
        val result = loginRepository.login(username, password, context)

        if (result is Result.Success) {
            _loginResult.postValue(LoginResult(success = true))

        }
    }
}
/**
 * Process to handle registration on a different thread
 */
class RegisterProcessRunnable(
    username: String,
    firstName: String,
    secondName: String,
    password: String,
    password2: String,
    context: Context,
    loginRepository: LoginRepository,
    _loginResult: MutableLiveData<LoginResult>) : Runnable {

    val username = username
    val firstName = firstName
    val secondName = secondName
    val password = password
    val password2 = password2
    val context = context
    val loginRepository = loginRepository
    val _loginResult = _loginResult
    val sessionManager = SessionManager(context)

    override fun run() {
        val result = loginRepository.register(username,firstName,secondName,password, password2,context)
        if (result is Result.Success) {
            _loginResult.postValue(LoginResult(success = true))

        }
    }
}
