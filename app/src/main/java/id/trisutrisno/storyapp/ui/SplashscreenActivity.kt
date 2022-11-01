package id.trisutrisno.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.ui.auth.LoginActivity
import id.trisutrisno.storyapp.utils.SharedViewModel
import id.trisutrisno.storyapp.utils.UserViewModelFactory

class SplashscreenActivity : AppCompatActivity() {

    private val sharedViewModel: SharedViewModel by viewModels{
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splashscreen)

        validateUser()
    }

    private fun validateUser() {
        sharedViewModel.fetchUser()
        sharedViewModel.user.observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}