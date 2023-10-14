package com.example.BookShopApp.ui.profile.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.BookShopApp.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {

    companion object {
        fun newInstance() = PermissionFragment()
    }

    private var binding: FragmentPermissionBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPermissionBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            textAccept.setOnClickListener {
                val permissons = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                requestPermissions(permissons, 1)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageDirectory()
            } else {
                Toast.makeText(context, "User ko cap quyen", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openImageDirectory() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*" // Loại tệp tin là ảnh
        }
        startActivityForResult(intent, 1)
    }
}