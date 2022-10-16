package com.ergea.datastoreapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ergea.datastoreapp.databinding.FragmentHomeBinding
import java.io.ByteArrayOutputStream

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: DataStoreManager
    private lateinit var viewModel: UserViewModel

    private val REQUEST_CODE_PERMISSION = 3
    private val GALLERY_RESULT_CODE = 15

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            viewModel.saveImage(result.toString())
        }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    private fun setProfileImage(bitmap: Bitmap) {
        binding.img1.setImageBitmap(bitmap)
    }

    private fun convertStringToBitmap(string: String): Bitmap {
        val imageBytes = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDataStoreUsername().observe(viewLifecycleOwner) {
            binding.tv1.text = "Hai " + it.toString()
        }
        viewModel.getDataStorePassword().observe(viewLifecycleOwner) {
            binding.tv2.text = "Password anda:" + it.toString()
        }
        binding.tv3.setOnClickListener {
            viewModel.isClear()
            it.findNavController().navigate(R.id.action_homeFragment2_to_loginFragment)
        }
        viewModel.getImageUser().observe(viewLifecycleOwner) {
            Log.d("profile", "observe $it")
            if (it.isNullOrEmpty().not()) {
                setProfileImage(convertStringToBitmap(it))
            }
        }
        imageOnClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_RESULT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val profileImage = data.data
            viewModel.saveImage(profileImage.toString())
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        Log.d("profile", "set ${bitmap}")
        viewModel.saveImage(bitMapToString(bitmap))
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun imageOnClick() {
        binding.img1.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        if (isGranted(
                this,
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION
            )
        ) {
            //openGallery()
            openCamera()
        }
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext()).setMessage("Pilih gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }.show()
    }


    private fun openGallery() {
        val imageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        imageIntent.type = "image/*"
        imageIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(imageIntent, GALLERY_RESULT_CODE)
        //galleryResult.launch(intent)
    }

    private fun isGranted(
        activity: HomeFragment,
        permission: String, //for camera
        permissions: Array<String>, //for read write storage/gallery
        request: Int
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(requireContext(), permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), permissions, request)
            }
            false
        } else {
            true
        }

    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission denied")
            .setMessage("Permission is denied, Please allow app permission from App Settings")
            .setPositiveButton("App Settings") { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", "packageName", null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}