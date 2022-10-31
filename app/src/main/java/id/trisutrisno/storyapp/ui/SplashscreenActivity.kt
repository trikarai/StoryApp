package id.trisutrisno.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.ui.auth.LoginActivity
import id.trisutrisno.storyapp.utils.UserViewModelFactory

class SplashscreenActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels{
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splashscreen)

        validateUser()
    }

    private fun validateUser() {
        viewModel.fetchUser().observe(this){ token ->
            if (token != ""){
                startActivity(Intent(this, MainActivity::class.java).putExtra(
                    MainActivity.EXTRA_TOKEN, token))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}