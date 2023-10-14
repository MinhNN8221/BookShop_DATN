package com.example.BookShopApp.ui.profile.updateprofile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.databinding.FragmentUpdateProfileBinding
import com.example.BookShopApp.utils.format.FormatDate
import com.example.BookShopApp.utils.LoadingProgressBar
import com.example.BookShopApp.utils.MySharedPreferences
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.*

class UpdateProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateProfileFragment()
    }

    private var binding: FragmentUpdateProfileBinding? = null
    private lateinit var viewModel: UpdateProfileViewModel
    private var customer_id: Int? = null
    private var avatar: String? = null
    private var shipping_region_id: Int? = null
    private lateinit var loadingProgressBar: LoadingProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingProgressBar = LoadingProgressBar(requireContext())
        loadingProgressBar.show()
        initViewModel()
        viewModel.getCustomer()
        activity?.let { MySharedPreferences.init(it.applicationContext) }
        binding?.apply {
            cardview.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (context?.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1)
                    } else {
                        MySharedPreferences.removePermissionDeniedCount()
                        openImageDirectory()
                    }
                } else {
                    if (context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1
                        )
                    } else {
                        openImageDirectory()
                    }
                }

            }
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            textUpdateProfile.setOnClickListener {
                updateProfie()
            }
            val myCalendar = Calendar.getInstance()
            var year = myCalendar.get(Calendar.YEAR)
            var month = myCalendar.get(Calendar.MONTH)
            var dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)
            editDob.setOnClickListener {
                if (editDob.text.toString() != "") {
                    val date = editDob.text.toString().split("/")
                    year = date[2].toInt()
                    month = date[1].toInt() - 1
                    dayOfMonth = date[0].toInt()
                }
                DatePickerDialog(
                    requireContext(),
                    { datePicker, year, month, dayOfMonth ->
                        val dateOfBirth = "$dayOfMonth/${month + 1}/$year"
                        editDob.setText(FormatDate().formatDateOfBirth(dateOfBirth))
                    }, year, month, dayOfMonth
                ).show()
            }
            layoutProfile.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val event =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    event.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        viewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                .show()
        })
        viewModel.profile.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                bindData(it)
                customer_id = it.customerId
                shipping_region_id = it.shippingRegionId
                avatar = it.avatar
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageDirectory()
                MySharedPreferences.removePermissionDeniedCount()
            } else {
                val permissionDeniedCount = MySharedPreferences.getPermissionDeniedCount()
                if (permissionDeniedCount > 3) {
                    showDialogToExplainWhyPermissionIsNeeded()
                } else {
                    incrementPermissionDeniedCount()
                    Toast.makeText(context, "User ko cap quyen", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showDialogToExplainWhyPermissionIsNeeded() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Cần truy cập hình ảnh")
            .setMessage("Ứng dụng cần truy cập hình ảnh để hiển thị ảnh của bạn.")
            .setPositiveButton("Đồng ý") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context?.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Hủy") { _, _ ->

            }
            .create()

        dialog.show()
    }

    private fun openImageDirectory() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*" // Loại tệp tin là ảnh
        }
        startActivityForResult(intent, 1)
    }

    private fun incrementPermissionDeniedCount() {
        val count = MySharedPreferences.getPermissionDeniedCount() + 1
        MySharedPreferences.putPermissionDeniedCount(count)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val picturePath = uri?.let { uriToFilePath(it) }
            val file = File(picturePath)
            val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val multiPart = MultipartBody.Part.createFormData("image", file.name, requestBody)
            viewModel.changeAvatar(multiPart)
            MySharedPreferences.putString("imageAvatar", picturePath.toString())
            binding?.imageAvatar?.setImageURI(uri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindData(profile: Customer) {
        val imgAvatar = MySharedPreferences.getString("imageAvatar", "")
        if (imgAvatar != "") {
            binding?.apply {
                Glide.with(root)
                    .load(imgAvatar)
                    .centerCrop()
                    .into(imageAvatar)
            }
        } else {
            if (profile.avatar == "") {
                binding?.imageAvatar?.setImageResource(R.drawable.account_profile)
            } else {
                binding?.apply {
                    Glide.with(root)
                        .load(profile.avatar)
                        .centerCrop()
                        .into(imageAvatar)
                }
            }
        }
        binding?.apply {
            editFullname.setText(profile.name)
            profile.dateOfBirth?.let {
                editDob.setText(FormatDate().formatDateOfBirthView(profile.dateOfBirth.toString()))
            }
            editPhone.setText(profile.mobPhone)
            editAddress.setText(profile.address)
            editEmail.setText(profile.email)
            if (profile.gender.equals("Nam")) {
                radiobtnNam.isChecked = true
            } else {
                radiobtnNu.isChecked = true
            }
            loadingProgressBar.cancel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateProfie() {
        val pattern = Regex("^0\\d{9}$")
        binding?.apply {
            val fullName = editFullname.text.toString()
            var gender = "Nữ"
            if (radiobtnNam.isChecked) {
                gender = "Nam"
            }
            val phone = editPhone.text.toString()
            val address = editAddress.text.toString()
            val checkPhone = pattern.matches(editPhone.text.toString())
            if (checkPhone && editDob.text.toString().isNotEmpty()) {
                val Dob = FormatDate().formatDateReverse(editDob.text.toString())
                viewModel.updateCustomer(fullName, address, Dob, gender, phone)
            } else if (editDob.text.toString().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter the correct format of the date of birthday!", Toast.LENGTH_SHORT
                ).show()
            } else if (!checkPhone) {
                Toast.makeText(
                    requireContext(),
                    "Please enter the correct format of the phone number!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun uriToFilePath(uri: Uri): String? {
        val inputStream = context?.contentResolver?.openInputStream(uri)
        inputStream?.use { inputStream ->
            val outputFile = createTempImageFile()
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { outputStream ->
                val buffer = ByteArray(4 * 1024) //4KB
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } >= 0) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                return outputFile.absolutePath
            }
        }
        return null
    }

    private fun createTempImageFile(): File {
        val tempFileName = "temp_image_${System.currentTimeMillis()}.jpg"
        val storageDir = context?.getExternalFilesDir(null)
        return File.createTempFile(tempFileName, ".jpg", storageDir)
    }
}