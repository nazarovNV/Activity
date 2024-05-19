package otus.gpb.homework.activities.receiver

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        val data1 = intent?.getStringExtra("data1")
        val data2 = intent.getStringExtra("data2")
        val data3 = intent.getStringExtra("data3")
        if (data1 != null) {
            Log.i("mytag", data1)
        }

    }
}