package id.trisutrisno.storyapp.ui.auth

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.databinding.ActivityRegisterBinding
import id.trisutrisno.storyapp.utils.Result
import id.trisutrisno.storyapp.utils.UserViewModelFactory

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupListener()
        playAnimation()
    }

    private fun setupListener() {
         binding.registerButton.setOnClickListener {
             val name = binding.nameInput.text.toString().trim()
             val email = binding.emailInput.text.toString().trim()
             val password = binding.passwordInput.text.toString().trim()

             when {
                 name.isEmpty() -> {
                     setNameError(getString(R.string.name_must_filled))
                 }
                 email.isEmpty() -> {
                     setEmailError(getString(R.string.email_must_filled))
                 }
                 password.length < 6 -> {
                     setPasswordError(getString(R.string.error_password_not_valid))
                 }
                 else -> {
                     register()
                 }
             }

         }
    }

    private fun setPasswordError(string: String?) {
        binding.passwordInput.error = string
    }

    private fun setEmailError(string: String?) {
        binding.emailInput.error = string
    }
    private fun setNameError(string: String?) {
        binding.nameInput.error = string
    }

    private fun onSuccess() {
        Snackbar.make(binding.root, getString(R.string.register_success), Snackbar.LENGTH_LONG).show()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun onFailed() {
        Snackbar.make(binding.root, getString(R.string.register_failed), Snackbar.LENGTH_LONG).show()
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
            binding.registerButton.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.registerButton.isEnabled = true
        }
    }

    private fun register() {
        registerViewModel.register(binding.nameInput.text.toString().trim(), binding.emailInput.text.toString().trim(), binding.passwordInput.text.toString().trim())
            .observe(this) {
            registerResponse ->
                when(registerResponse) {
                    is Result.Loading -> {
                        onLoading(true)
                    }
                    is Result.Success -> registerResponse.data.let {
                        onLoading(false)
                        onSuccess()
                    }
                    is Result.Error -> registerResponse.data.let {
                        onLoading(false)
                        onFailed()
                    }
                }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogo, View.TRANSLATION_X, -60f, 60f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}