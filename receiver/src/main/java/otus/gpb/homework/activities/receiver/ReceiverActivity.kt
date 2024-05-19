package otus.gpb.homework.activities.receiver

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat



class ReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        val titledata = intent?.getStringExtra("title")
        val yeardata = intent.getStringExtra("year")
        val descriptiondata = intent.getStringExtra("description")


        val posterImageView = findViewById<ImageView>(R.id.posterImageView)
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val yearTextView = findViewById<TextView>(R.id.yearTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)

        if(titledata == "Славные парни")
            posterImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.niceguys))
        else
            posterImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.interstellar))

        // val a = Context.getDrawable() - не работает, не понятно почему
        titleTextView.text = titledata
        yearTextView.text = yeardata
        descriptionTextView.text = descriptiondata



    }
}