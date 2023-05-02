package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}