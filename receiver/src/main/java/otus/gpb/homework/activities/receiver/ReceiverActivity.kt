package otus.gpb.homework.activities.receiver

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        val data1 = intent?.getStringExtra("data1")
        val data2 = intent.getStringExtra("data2")
        val data3 = intent.getStringExtra("data3")

        val posterImageView = findViewById<ImageView>(R.id.posterImageView)
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val yearTextView = findViewById<TextView>(R.id.yearTextView)


    }
}