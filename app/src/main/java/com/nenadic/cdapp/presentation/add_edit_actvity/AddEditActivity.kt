package com.nenadic.cdapp.presentation.add_edit_actvity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.nenadic.cdapp.R
import com.nenadic.cdapp.databinding.ActivityAddEditBinding
import com.nenadic.cdapp.domian.model.CD
import com.nenadic.cdapp.domian.utils.constants.ALBUM_NAME
import com.nenadic.cdapp.domian.utils.constants.ARTIST_NAME
import com.nenadic.cdapp.domian.utils.constants.CD_ID
import com.nenadic.cdapp.domian.utils.constants.DATE
import com.nenadic.cdapp.domian.utils.constants.DATE_FORMATE
import com.nenadic.cdapp.domian.utils.constants.IMAGE
import com.nenadic.cdapp.domian.utils.constants.ORIGIN
import com.nenadic.cdapp.domian.utils.constants.dateStringToLong
import com.nenadic.cdapp.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AddEditActivity : BaseActivity() {
    @Inject
    lateinit var binding: ActivityAddEditBinding
    private val viewModel: AddEditViewModel by viewModels()
    private lateinit var currentPhotoPath: String
    private var uri: Uri? = null
    private var labelOrigin = ""
    private var id = 0
    private val calendar = Calendar.getInstance()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uri = Uri.fromFile(File(currentPhotoPath))
                binding.ivCd.setImageURI(uri)
//                Log.d("cvv", ": $uri  date: ${binding.etDate.selectAll()}")
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        id = intent.getIntExtra(CD_ID, 0)
        if (id != 0) {
            setViewForEdit()
        }
        allClicks()
    }

    private fun setViewForEdit() {
        val albumName = intent.getStringExtra(ALBUM_NAME)
        val artistName = intent.getStringExtra(ARTIST_NAME)
        val date = intent.getStringExtra(DATE)
        val labelOrigin = intent.getStringExtra(ORIGIN)
        val image = intent.getStringExtra(IMAGE)
        with(binding) {
            title.text = getString(R.string.edit_cd)
            save.text = getString(R.string.update)
            etAlbumName.setText(albumName)
            etArtistName.setText(artistName)
            etDate.text = date
            ivCd.setImageURI(image?.toUri())
            uri = image?.toUri()
            if (labelOrigin == "Domestic") {
                domesticRadioButton.isChecked = true
            } else
                foreignRadioButton.isChecked = true
        }
    }


    private fun allClicks() {
        with(binding) {
            takeImage.setOnClickListener {
                takePics()
            }

            originRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                val radioButton: RadioButton = findViewById(checkedId)
                labelOrigin = radioButton.text.toString()
            }
            val selectedRadioButtonId = originRadioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                labelOrigin = selectedRadioButton.text.toString()
            }
            save.setOnClickListener {
                saveCD()
            }
            close.setOnClickListener {
                callBackPress()
            }
            etDate.setOnClickListener {
                showDatePicker()
            }
        }
    }

    private fun takePics() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun saveCD() {
        if (authentication()) {
            with(binding) {
                val etAlbumName = etAlbumName.text.toString()
                val etArtistName = etArtistName.text.toString()
                val dateInString = etDate.text.toString()
                val etDate = dateStringToLong(dateString = dateInString, dateFormat = DATE_FORMATE)
                val cd = if (id == 0) {
                    CD(
                        picture = uri.toString(),
                        albumName = etAlbumName,
                        artistName = etArtistName,
                        releaseDate = etDate,
                        labelOrigin = labelOrigin
                    )
                } else {
                    CD(
                        id = id,
                        picture = uri.toString(),
                        albumName = etAlbumName,
                        artistName = etArtistName,
                        releaseDate = etDate,
                        labelOrigin = labelOrigin
                    )
                }
                Log.d("cvv", "saveCD: $cd")
                viewModel.insertCD(cd)

                Toast.makeText(this@AddEditActivity, "Save Successfully", Toast.LENGTH_SHORT)
                    .show()
                setResult(RESULT_OK)
                finish()


            }
        } else {
            Toast.makeText(this, "Please fill all field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun authentication(): Boolean {
        with(binding) {
            return etDate.text.isNotBlank()
                    && etArtistName.text.isNotBlank()
                    && etAlbumName.text.isNotBlank()
                    && uri != null
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: Exception) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(
                            this, "${packageName}.provider", it
                        )
                    } else {
                        Uri.fromFile(it)
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = System.currentTimeMillis().toString()
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun callBackPress() {
        finish()
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat(DATE_FORMATE, Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                binding.etDate.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }
}