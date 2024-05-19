package otus.gpb.homework.activities.sender

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity



class SenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sender_activity)

        val buttonMaps = findViewById<Button>(R.id.buttonMaps)
        buttonMaps.setOnClickListener{
            val gmmIntentUri = Uri.parse("geo: 0,0?q= Рестораны")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        val buttonEmail = findViewById<Button>(R.id.buttonEmail)
        buttonEmail.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:android@otus.ru?subject=Тема письма&body=Привет! Это тестовое письмо.")
            }
            startActivity(emailIntent)

        }

        val buttonReceiver = findViewById<Button>(R.id.buttonReceiver)
        buttonReceiver.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("title", "Славные парни")
            intent.putExtra("year", "2016")
            intent.putExtra("description", "Что бывает, когда напарником брутального " +
                    "костолома становится субтильный лопух? Наемный охранник Джексон Хили " +
                    "и частный детектив Холланд Марч вынуждены работать в паре, чтобы распутать " +
                    "плевое дело о пропавшей девушке, которое оборачивается преступлением века. " +
                    "Смогут ли парни разгадать сложный ребус, если у каждого из них – свои, " +
                    "весьма индивидуальные методы.")

            val chooser = Intent.createChooser(intent, "Receive a movie")
            startActivity(chooser)
        }

    }
}