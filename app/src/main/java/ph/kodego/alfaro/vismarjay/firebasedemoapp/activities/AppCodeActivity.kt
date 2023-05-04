package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityAppCodeBinding

class AppCodeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAppCodeBinding
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database
        val codeRef = database.getReference("AppCode")

        binding.submitButton.setOnClickListener{
            codeRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var codeFound = false
                    val userCode = binding.appCode.text.toString().trim()

                    for (childSnapshot in snapshot.children) {
                        val codeValue = childSnapshot.getValue(String::class.java)

                        Log.i("FirebaseDemo", "userCode = $userCode, codeValue = $codeValue")

                        if (codeValue?.trim() == userCode.trim()) {
                            // The input code matches a value in the database
                            codeFound = true
                            Log.i("FirebaseDemo", "Code found")
                            performSignup()

                            childSnapshot.ref.removeValue()
                            break
                        }
                    }

                    if (!codeFound) {
                        // The input code does not match any value in the database
                        Log.d("FirebaseDemo", "Code not found")
                        toast()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

    }

    fun toast(){
        Toast.makeText(this,"Code Invalid",Toast.LENGTH_SHORT).show()

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