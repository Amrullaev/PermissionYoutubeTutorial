package com.example.permissionyoutubetutorial

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.permissionyoutubetutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var cameraPermissionGranted = false
    var readContactPermissionGranted = false

    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsStates ->

            if (permissionsStates[Manifest.permission.CAMERA] == true) {
                Toast.makeText(this, "Camera permission is granted", Toast.LENGTH_SHORT)
                    .show()
                updatePermissionState()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) && permissionsStates.containsKey(
                        Manifest.permission.CAMERA
                    )
                ) {
                    AlertDialog.Builder(this)
                        .setTitle("Permission is permanently denied")
                        .setMessage("If you wish to continue and enabling this feature, you could go and enable it manually buy click ok")
                        .setPositiveButton("OK") { dialog, _ ->

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.fromParts("package", packageName, null)
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                }
            }

            if (permissionsStates[Manifest.permission.READ_CONTACTS] == true && permissionsStates.containsKey(
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                Toast.makeText(this, "Read contact permission is granted", Toast.LENGTH_SHORT)
                    .show()
                updatePermissionState()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder(this)
                        .setTitle("Permission is permanently denied")
                        .setMessage("If you wish to continue and enabling this feature, you could go and enable it manually buy click ok")
                        .setPositiveButton("OK") { dialog, _ ->

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.fromParts("package", packageName, null)
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                }
            }

        }

    override fun onResume() {
        super.onResume()
        updatePermissionState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.checkReadContacsPermission.setOnClickListener {
            if (readContactPermissionGranted) {
                Toast.makeText(
                    this,
                    "Read contacts permission is alredy granted",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder(this)
                    .setTitle("Why we need this permission")
                    .setMessage("We need the read contacts permission to make it simple the access your contacts within the app")
                    .setPositiveButton("OK") { dialog, _ ->
                        permissionLauncher.launch(arrayOf(Manifest.permission.READ_CONTACTS))
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                permissionLauncher.launch(arrayOf(Manifest.permission.READ_CONTACTS))
            }
        }

        binding.checkCameraPermission.setOnClickListener {
            if (readContactPermissionGranted) {
                Toast.makeText(
                    this,
                    "Camera permission is alredy granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this)
                    .setTitle("Why we need this permission")
                    .setMessage("We need the camera permission to capture images and assigned to each contacts within the application")
                    .setPositiveButton("OK") { dialog, _ ->
                        permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))

            }
        }
    }

    private fun updatePermissionState() {
        cameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        readContactPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        val permissionStates = when {
            cameraPermissionGranted && readContactPermissionGranted -> "Read contacts and camera permission are granted"
            cameraPermissionGranted -> "Only camera permission is granted"
            readContactPermissionGranted -> "Only read contacts permission is granted"
            else -> "No permission is granted"
        }
        binding.permissionStatusTv.text = permissionStates

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }
}