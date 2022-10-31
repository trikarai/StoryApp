package id.trisutrisno.storyapp.ui.auth

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.databinding.ActivityLoginBinding
import id.trisutrisno.storyapp.domain.model.toLoggedInUser
import id.trisutrisno.storyapp.ui.SharedViewModel
import id.trisutrisno.storyapp.utils.Result
import id.trisutrisno.storyapp.utils.UserViewModelFactory

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }
    private val sharedViewModel: SharedViewModel by viewModels {
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
                    loginViewModel.login(email, password)
                }
            }
        }
    }


    private fun setEmailError(e : String?){
        binding.editTextTextEmail.error = e
    }

    private fun setPasswordError(e: String?){
        binding.editTextTextPassword.error = e
    }



    private fun setupObserver() {
        loginViewModel.login(binding.editTextTextEmail.toString(), binding.editTextTextPassword.toString())
            .observe(this) { loginResult ->

                when (loginResult) {
                    is Result.Loading -> {
                        setLoading(true)
                    }
                    is Result.Success -> loginResult.data?.let {
                        setLoading(false)
                        sharedViewModel.saveUser(it.toLoggedInUser())
                    }
                    is Result.Error -> {
                        setLoading(false)
                        Snackbar.make(binding.root, getString(R.string.login_failed), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

            }
    }



    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.button.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.button.isEnabled = true
        }
    }

    private fun playAnimation() {
         ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -60f, 60f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }


}