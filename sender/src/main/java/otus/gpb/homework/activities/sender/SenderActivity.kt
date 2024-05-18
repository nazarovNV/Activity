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
            val intent = Intent("testActivity")
            startActivity(intent)



        }

    }
}