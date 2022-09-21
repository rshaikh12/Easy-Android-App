package com.example.easy.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.easy.data.model.LoggedInUser
import com.example.easy.data.model.LoginUser
import com.example.easy.rest.AccountRestApiService
import com.example.easy.rest.ProfileRestApiService
import com.example.easy.rest.SessionManager
import java.io.IOException
import java.lang.Exception

/**
 * @author Marius Funk, preset
 *
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 *
 * This class was included in our chosen inital project.
 * I added some aspects but didn't see the need to use "Repository" in other activities.
 *
 */

class LoginRepository() {


    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    private lateinit var sessionManager: SessionManager

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    /**
     * Unused
     */
    fun logout() {
        user = null
    }

    /**
     * Logs in the user, and saves the token to the SessionManager
     */
    fun login(username: String, password: String, context: Context): Result<LoggedInUser> {
        try {
            println("${Thread.currentThread()} has run.")
            val userResponse = AccountRestApiService().login(username, password)

            SessionManager(context).saveAuthToken(userResponse!!.token)
            val profileResponse =
                ProfileRestApiService().getProfileByLoginId(userResponse.loggedInUser.loginId)
            user = profileResponse

            if (user == null) {
                return Result.Error(Exception("Request timed out, login failed"))
            }
            setLoggedInUser(user!!)
            return Result.Success(user!!)
        } catch (e: Throwable) {
            return Result.Error(Exception("Error logging in:", e))
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
    /**
     * Registers the user, then logs him/her in and saves the token to the SessionManager
     */
    fun register(
        email: String,
        firstName: String,
        secondName: String,
        password: String,
        password2: String,
        context: Context
    ): Result<LoggedInUser> {
        try {
            AccountRestApiService().addUser(
                LoginUser(
                    "",
                    firstName,
                    secondName,
                    email,
                    password,
                    password2
                )
            )
            //Login after registration
            return login(email, password, context)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error registering", e))
        }
    }


}


class LoginRunnable(
    username: String,
    password: String,
    context: Context,
    user: LoggedInUser?
) : Runnable {

    val username = username
    val password = password
    val context = context
    val sessionManager = SessionManager(context)

    //Fake Dummy
    @Volatile
    var liveUser: LoggedInUser? =
        user//LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe", "Jane", "Doe", "jane@doe.com", "123", 42.0, 42.0  )

    public override fun run() {
        try {
            println("${Thread.currentThread()} has run.")
            // TODO: handle loggedInUser authentication (TOKEN)
            val userResponse = AccountRestApiService().login(this.username, password)

            sessionManager.saveAuthToken(userResponse!!.token)
            val profileResponse =
                ProfileRestApiService().getProfileByLoginId(userResponse.loggedInUser.loginId)
            liveUser = profileResponse
        } catch (e: Throwable) {
            Log.e("Error login in", e.toString())
        }
    }
}