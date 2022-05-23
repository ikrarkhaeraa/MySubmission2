package com.example.mysubmission1.main.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mysubmission1.R
import com.example.mysubmission1.databinding.ActivityAddStoryBinding
import com.example.mysubmission1.main.*
import com.example.mysubmission1.main.model.AddNewStoryModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var factory: ModelFactory
    private val model: AddNewStoryModel by viewModels { factory }
    private var getMyFile: File? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ModelFactory.getInstance(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        getLocation()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Location permission granted
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    this.location = location
                    Log.d("AddStory", "getLastLocation: ${location.latitude}, ${location.longitude}")
                } else {
                    Toast.makeText(
                        this,
                        "Location Error",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.buttonLocation.isChecked = false
                }
            }
        } else {
            // Location permission denied
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.d("AddStory", "$permissions")
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLocation()
            }
            else -> {
                Snackbar
                    .make(
                        binding.root,
                        "Location Error",
                        Snackbar.LENGTH_SHORT
                    )
                    //.setActionTextColor(getColor(R.color.white))
                    .setAction("Switch Button") {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                            val uri = Uri.fromParts("uri", packageName, null)
                            intent.data = uri
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    .show()
                binding.buttonLocation.isChecked = false
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.mysubmission1.main",
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

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            getMyFile = myFile
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            //val result =  BitmapFactory.decodeFile(getMyFile?.path)
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getMyFile?.path),
                isBackCamera
            )

            binding.previewImageView.setImageBitmap(result)
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getMyFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(getMyFile?.path),
                true
            )

            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getMyFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        model.getUserSession().observe(this@AddStoryActivity) {
            if (getMyFile != null) {
                val file = reduceFileImage(getMyFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                var lat: RequestBody? = null
                var lon: RequestBody? = null
                if (location != null) {
                    lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }
                Log.d("upload", "response: ${file}")
                uploadResponse(
                    it.token,
                    imageMultipart,
                    binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaType()),
                    lat,
                    lon
                )
            } else {
                Toast.makeText(applicationContext, "Input Image First", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadResponse(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) {
        model.uploadStory(token, file, description, lat, lon)
        model.upload.observe(this@AddStoryActivity) {
            if (!it.error) {
                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
