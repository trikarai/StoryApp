package id.trisutrisno.storyapp.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.databinding.ActivityUploadStoryBinding
import id.trisutrisno.storyapp.utils.UserViewModelFactory
import id.trisutrisno.storyapp.utils.Result
import id.trisutrisno.storyapp.utils.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding

    private lateinit var token: String
    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var getFile: File? = null
    private var location: Location? = null

    private val uploadStoryViewModel: UploadStoryViewModel by viewModels(){
        UserViewModelFactory.getInstance(this)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK){
            val myFile =File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(myFile.path)

            binding.imagePreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            val selectedImage: Uri = result.data?.data as Uri
            val myFile = Utils.uriToFile(selectedImage, this@UploadStoryActivity)
            getFile = myFile

            binding.imagePreview.setImageURI(selectedImage)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getCurrentLocation()
            }
            else -> {
                Snackbar.make(binding.root, getString(R.string.location_permission_denied), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.location_permission_denied_action)) {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    .show()
                binding.switchLoc.isChecked = false
            }
        }
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupObserver()
        setupListener()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, getString(R.string.upload_permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        Utils.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadStoryActivity,
                "id.trisutrisno.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun setupObserver() {
        uploadStoryViewModel.fetchUser().observe(this){ userToken ->
            if (userToken != "") {
                token = userToken
            }
        }
        uploadStoryViewModel.uploadResponse.observe(this){ response ->
            when(response) {
                is Result.Loading -> {
                    onLoading(true)
                }
                is Result.Success -> response.data.let {
                    onLoading(false)
                    Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Error -> response.data.let {
                    onLoading(false)
                    Toast.makeText(this, getString(R.string.upload_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListener(){
        binding.buttonCamera.setOnClickListener {
            startTakePhoto()
        }
        binding.buttonGallery.setOnClickListener {
            startGallery()
        }
        binding.buttonUpload.setOnClickListener {
            uploadStory()
        }
        binding.switchLoc.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                getCurrentLocation()
            } else {
                this.location = null
            }
        }
    }

    private fun getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ){
            fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null){
                    this.location = currentLocation
                } else {
                    Toast.makeText(this, getString(R.string.please_activate_location_message), Toast.LENGTH_SHORT).show()
                    binding.switchLoc.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }


    private fun uploadStory() {
        val inputDescription =  binding.inputDesc
        var isValid = true

        if (inputDescription.text.toString().isBlank()) {
            inputDescription.error = getString(R.string.upload_desc)
            isValid = false
        }
        if (getFile == null) {
            Toast.makeText(this, getString(R.string.upload_image), Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (isValid) {
            val file = reduceFileImage(getFile as File)
            val description = inputDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )
            var lat: RequestBody? = null
            var lon: RequestBody? = null

            if (location != null) {
                lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
            }

            uploadStoryViewModel.uploadStory(token, imageMultipart, description, lat, lon)
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}