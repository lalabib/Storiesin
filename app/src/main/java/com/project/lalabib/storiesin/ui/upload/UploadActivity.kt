package com.project.lalabib.storiesin.ui.upload

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.project.lalabib.storiesin.R
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.data.model.dataStore
import com.project.lalabib.storiesin.databinding.ActivityUploadBinding
import com.project.lalabib.storiesin.ui.main.MainActivity
import com.project.lalabib.storiesin.utils.ViewModelFactory
import com.project.lalabib.storiesin.utils.createCustomTempFile
import com.project.lalabib.storiesin.utils.reduceFileImage
import com.project.lalabib.storiesin.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var uploadViewModel: UploadViewModel

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        permission()

        binding.apply {
            btnCamera.setOnClickListener { startTakePhoto() }
            btnGallery.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener { postStory() }
        }
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_upload)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewModel() {
        val pref = UserPreference.getInstance(dataStore)
        uploadViewModel = ViewModelProvider(this, ViewModelFactory(pref))[UploadViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this@UploadActivity, R.string.permission, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun permission() {
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(applicationContext).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.project.lalabib.storiesin",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@UploadActivity)

            getFile = myFile

            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun postStory() {
        showLoading()
        val desc = binding.addDescription.text.toString()
        uploadViewModel.getUser().observe(this@UploadActivity) {
            if (getFile != null && desc != "") {
                val file = reduceFileImage(getFile as File)
                val addDesc = desc.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                uploadStory(it.token, imageMultipart, addDesc)
            } else {
                if (getFile == null) Toast.makeText(this@UploadActivity, R.string.please_add_image, Toast.LENGTH_SHORT).show()
                if (desc == "") Toast.makeText(this@UploadActivity, R.string.cant_empty, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        uploadViewModel.postStory(token, file, description)
        uploadViewModel.addStoryResponse.observe(this@UploadActivity) {
            if (!it.error) {
                moveToMain()
            }
        }
        showToast()
    }

    private fun moveToMain() {
        startActivity(Intent(this@UploadActivity, MainActivity::class.java))
        finish()
    }

    private fun showToast() {
        uploadViewModel.txtMsg.observe(this@UploadActivity) {
            it.getContentIfNotHandled()?.let {  txtMsg ->
                Toast.makeText(this, txtMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        uploadViewModel.loading.observe(this@UploadActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}