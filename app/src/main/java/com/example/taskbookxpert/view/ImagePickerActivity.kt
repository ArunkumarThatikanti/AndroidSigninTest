package com.example.taskbookxpert.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.taskbookxpert.R

class ImagePickerActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var count = 0
    var count1 = 0

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value == true }
        if (!allGranted) {
            Toast.makeText(this, "Please grant all permissions", Toast.LENGTH_SHORT).show()
        }
    }


    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let { uri ->
                imageView.load(uri) {
                    crossfade(true)
                    placeholder(R.drawable.baseline_image_24)
                    error(R.drawable.baseline_broken_image_24)
                }
            }

        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            bitmap?.let {
                imageView.load(it) {
                    crossfade(true)
                    placeholder(R.drawable.baseline_image_24)
                    error(R.drawable.baseline_broken_image_24)
                }
            }        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

//        checkAndRequestPermissions()

        val btnCamera = findViewById<Button>(R.id.btnCamera)
        val btnGallery = findViewById<Button>(R.id.btnGallery)
        imageView = findViewById(R.id.imageView)

        btnCamera.setOnClickListener {
            count = sharedPref.getInt("permission_count",0)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(intent)
            }
            else{
                if(count<1){
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.CAMERA), 1000)
                    count++
                    editor.putInt("permission_count",count).apply()
                }
                else{
                    requestPermission()
                }
            }
        }

        btnGallery.setOnClickListener {
            count1 = sharedPref.getInt("permission_count1",0)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryLauncher.launch(intent)
                }
                else{
                    if(count<1){
                        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 1000)
                        count++
                        editor.putInt("permission_count",count).apply()
                    }
                    else{
                        requestPermissiongallery()
                    }
                }
            }
            else{
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryLauncher.launch(intent)
                }
                else{
                    if(count<1){
                        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                        count++
                        editor.putInt("permission_count",count).apply()
                    }
                    else{
                        requestPermissiongallery()
                    }
                }
            }


        }

    }

    private fun requestPermissiongallery() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)
            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(this)
                .setTitle("Camera Permission Required")
                .setMessage("This feature needs access to your camera. Please allow the permission.")
                .setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        } else {
            // Ask again
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("Camera permission has been denied permanently. Please enable it from app settings.\n" +
                        "1. Open Settings\n" +
                        "2. Click on Permissions\n" +
                        "3. Select Photos and Videos (or) storage to allow Storage Permissions")
                .setPositiveButton("Go to Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show()
        }    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            AlertDialog.Builder(this)
                .setTitle("Camera Permission Required")
                .setMessage("This feature needs access to your camera. Please allow the permission.")
                .setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1000)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        } else {
            // Ask again
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("Camera permission has been denied permanently. Please enable it from app settings.\n" +
                        "1. Open Settings\n" +
                        "2. Click on Permissions\n" +
                        "3. Select Camera to allow Camera Permissions")
                .setPositiveButton("Go to Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }
}