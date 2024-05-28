package otus.gpb.homework.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.provider.Settings

class EditProfileActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var imageView: ImageView

    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    private var IS_FIRST_TIME_ALREADY_DENIED : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)

        imageView.setOnClickListener{
            showAlertDialog()
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                populateImage(uri)
            }
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Разрешение на использование камеры получено
                imageView.setImageResource(R.drawable.cat)
            }
            else {
                // Пользователь отказал в доступе к камере
                if (IS_FIRST_TIME_ALREADY_DENIED) {
                    showSettingsDialog()
                } else {
                    IS_FIRST_TIME_ALREADY_DENIED = true
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
    }

    /**
     * Используйте этот метод чтобы отобразить картинку полученную из медиатеки в ImageView
     */
    private fun populateImage(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        imageView.setImageBitmap(bitmap)
    }

    private fun openSenderApp() {
        TODO("В качестве реализации метода отправьте неявный Intent чтобы поделиться профилем. В качестве extras передайте заполненные строки и картинку")
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
            }
            else -> {
                if (IS_FIRST_TIME_ALREADY_DENIED)
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