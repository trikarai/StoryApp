package id.trisutrisno.storyapp.ui.auth

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.data.remote.user.login.LoginRequest
import id.trisutrisno.storyapp.data.remote.user.login.LoginResult
import id.trisutrisno.storyapp.databinding.ActivityLoginBinding
import id.trisutrisno.storyapp.ui.story.StoryActivity
import id.trisutrisno.storyapp.utils.UserViewModelFactory
import id.trisutrisno.storyapp.utils.Result

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        setupListener()
        setupObserver()
        playAnimation()

    }
    private fun setupListener() {
        binding.button.setOnClickListener{
            val email = binding.editTextTextEmail.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            when{
                email.isEmpty() -> {
                    setEmailError(getString(R.string.must_filled))
                }
                password.length < 6 -> {
                    setPasswordError(getString(R.string.error_password_not_valid))
                }
                else -> {
                    val login = LoginRequest(email, password)
                    loginViewModel.login(login)
                }
            }
        }
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setEmailError(e : String?){
        binding.editTextTextEmail.error = e
    }

    private fun setPasswordError(e: String?){
        binding.editTextTextPassword.error = e
    }



    private fun setupObserver() {
        loginViewModel.loginResponse.observe(this) { loginResponse ->
            when(loginResponse) {
                is Result.Loading -> {
                    onLoading(true)
                }
                is Result.Success -> loginResponse.data?.loginResult?.let {
                    onLoading(false)
                    onSuccess(it)
                }
                is Result.Error -> loginResponse.data.let {
                    onLoading(false)
                    onFailed()
                }
            }
        }
    }

    private fun onFailed() {
        Snackbar.make(binding.root, getString(R.string.login_failed), Snackbar.LENGTH_LONG).show()
    }

    @SuppressLint("StringFormatInvalid")
    private fun onSuccess(it: LoginResult) {
        loginViewModel.saveUser(it.token)
        Toast.makeText(this, getString(R.string.login_success, it.name), Toast.LENGTH_LONG).show()
        val intent = Intent(this@LoginActivity, StoryActivity::class.java)
        intent.putExtra(StoryActivity.EXTRA_TOKEN, it.token)
        startActivity(intent)
        finish()
    }

    private fun playAnimation() {
         ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -60f, 60f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }


}