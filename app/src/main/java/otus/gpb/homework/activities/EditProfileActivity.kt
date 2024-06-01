package otus.gpb.homework.activities

import android.Manifest
import android.R.id.message
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder


val RESULT_NAME = "result_name"
val RESULT_SECOND_NAME = "result_second_name"
val RESULT_AGE = "result_age"



class EditProfileActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var uri1: Uri

    private lateinit var imageView: ImageView

    private lateinit var buttonEditProfile: Button

    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    private var isFirstTimeDenied : Boolean = false

    private val launcherFillFormActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        val data = result.data
        val resultCode = result.resultCode
        if (resultCode == RESULT_OK && data!= null) {
            val name = data.getStringExtra(RESULT_NAME)
            val secondName = data.getStringExtra(RESULT_SECOND_NAME)
            val age = data.getStringExtra(RESULT_AGE)

            findViewById<TextView>(R.id.textview_name).text = name
            findViewById<TextView>(R.id.textview_second_name).text = secondName
            findViewById<TextView>(R.id.textview_age).text = age
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)

        imageView.setOnClickListener{
            showAlertDialog()
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                uri1 = uri
                populateImage(uri)
            }
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Разрешение на использование камеры получено
                imageView.setImageResource(R.drawable.cat)
                // Не понимаю как получить uri картинки с котом чтобы потом переслать ее в тг
                uri1 = Uri.parse("android.resource://$packageName/${R.drawable.cat}")
                Log.i("TAG", uri1.toString())
            }
            else {
                // Пользователь отказал в доступе к камере
                if (isFirstTimeDenied) {
                    showSettingsDialog()
                } else {
                    isFirstTimeDenied = true
                }
            }
        }

        findViewById<Toolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.send_item -> {
                        openSenderApp()
                        true
                    }
                    else -> false
                }
            }
        }

        buttonEditProfile = findViewById(R.id.buttonEditProfile)

        buttonEditProfile.setOnClickListener {

            val intent = Intent(this, FillFormActivity::class.java)
            launcherFillFormActivity.launch(intent)
        }

    }

    /**
     * Используйте этот метод чтобы отобразить картинку полученную из медиатеки в ImageView
     */
    private fun populateImage(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        imageView.setImageBitmap(bitmap)
    }

    private fun openSenderApp() {
        val nameToSend = findViewById<TextView>(R.id.textview_name).text
        val secondNameToSend = findViewById<TextView>(R.id.textview_second_name).text
        val ageToSend = findViewById<TextView>(R.id.textview_age).text

        val waIntent = Intent(Intent.ACTION_SEND)
        waIntent.type = "image/*"
        waIntent.setPackage("org.telegram.messenger")

        waIntent.putExtra(Intent.EXTRA_TEXT, "$nameToSend\n$secondNameToSend\n$ageToSend")
        waIntent.putExtra(Intent.EXTRA_STREAM, uri1) // Добавляем URI изображения
        Log.i("TAG", uri1.toString())
        startActivity(Intent.createChooser(waIntent, "Share with"))

    }


    private fun showAlertDialog()  {
        val addPhotoDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Добавить фото")
            .setMessage("Выберите откуда вы хотите добавить фото")
            .setNeutralButton("Назад") { dialog, which ->
                dialog.dismiss()
            }
            .setNegativeButton("Сделать фото") { dialog, which ->
                checkCameraPermission()
            }
            .setPositiveButton("Выбрать фото") { dialog, which ->
                choosePhotoFromGallery()
                // Respond to positive button press
            }.create()
        addPhotoDialog.show()
    }



    private fun choosePhotoFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                // Разрешение на использование камеры уже есть
                imageView.setImageResource(R.drawable.cat)

                val resID = resources.getIdentifier("cat", "drawable", packageName)
                Log.i("TAG", resID.toString())

            }
            else -> {
                if (isFirstTimeDenied)
                    showRationaleDialog()
                else
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    private fun showRationaleDialog() {
        val warningDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Необходимо разрешение")
            .setMessage("Для того чтобы сделать фото необходимо разрешение на использование камеры")
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Дать доступ") { dialog, _ ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                dialog.dismiss()
            }.create()
        warningDialog.show()

    }

    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Отказано в доступе")
            .setMessage("Вы отказали в доступе к камере. Хотите изменить это в настройках?")
            .setPositiveButton("Да") { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



}