package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class FillFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_form)

        findViewById<Button>(R.id.buttonApply).setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextTextName).text.toString()
            val secondName = findViewById<EditText>(R.id.editTextTextSecondName).text.toString()
            val age = findViewById<EditText>(R.id.editTextTextAge).text.toString()

            val intent = Intent()
                .putExtra(RESULT_NAME, name)
                .putExtra(RESULT_SECOND_NAME, secondName)
                .putExtra(RESULT_AGE, age)

            setResult(RESULT_OK, intent)

            Log.i("TAG", age)
            finish()
        }
    }
}