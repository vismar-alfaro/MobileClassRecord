package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database

        auth = Firebase.auth

        binding.tvSignup.setOnClickListener{
//            performSignup()
            val intent = Intent(this,AppCodeActivity::class.java)
            startActivity(intent)

        }

        binding.buttonLogin.setOnClickListener{
            performLogin()
        }

        binding.cbShowPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private fun performLogin() {
        val username: EditText = binding.etUsername
        val password: EditText = binding.etPassword

        if (username.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val usernameInput = username.text.toString()
        val passwordInput = password.text.toString()

        auth.signInWithEmailAndPassword(usernameInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    when (binding.roleGroup.checkedRadioButtonId) {
                        binding.instructorRadioButton.id -> {
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                        }
                        binding.studentRadioButton.id -> {
                            val intent = Intent(this, StudentDashboardActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {
                            Toast.makeText(this, "Please select a user type", Toast.LENGTH_SHORT).show()
                        }
                    }

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(
                        baseContext, "Authentication Failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    baseContext, "Authentication Failed. ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    private fun performSignup(){

        when (binding.roleGroup.checkedRadioButtonId) {
            binding.instructorRadioButton.id -> {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
            binding.studentRadioButton.id -> {
                val intent = Intent(this, StudentSignupActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Toast.makeText(this, "Please select a user type", Toast.LENGTH_SHORT).show()
            }
        }

    }



}