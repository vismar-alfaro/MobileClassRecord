package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var insertdata: Button
    private lateinit var fetchdata: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insertdata = findViewById(R.id.insert_data)
        fetchdata = findViewById(R.id.fetch_data)

        insertdata.setOnClickListener{
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        fetchdata.setOnClickListener{
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)

        }
    }
}