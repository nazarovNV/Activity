package otus.gpb.homework.activities

import android.Manifest
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

class EditProfileActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var imageView: ImageView

    private var IS_FIRST_TIME_ALREADY_DENIED : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)

        imageView.setOnClickListener{
            showAlertDialog()
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Разрешение на использование камеры получено
                imageView.setImageResource(R.drawable.cat)
            }
            else {
                // Пользователь отказал в доступе к камере
                showRationaleDialog()
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
                // Respond to neutral button press
            }
            .setNegativeButton("Сделать фото") { dialog, which ->
                checkCameraPermission()
            }
            .setPositiveButton("Выбрать фото") { dialog, which ->
                // Respond to positive button press
            }.create()
        addPhotoDialog.show()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                // Разрешение на использование камеры уже есть
                imageView.setImageResource(R.drawable.cat)
            }
            else -> {
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



}