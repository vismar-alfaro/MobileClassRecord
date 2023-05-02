package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityStudentSignupBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.Student
import java.util.*

class StudentSignupActivity : AppCompatActivity() {

    private lateinit var binding:ActivityStudentSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var studentRef: DatabaseReference
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private val storage = FirebaseStorage.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        database = Firebase.database
        userRef = database.getReference("users")
        studentRef = database.getReference("Students")
        auth = Firebase.auth


        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSignup.setOnClickListener {
            performSignUp()
        }

        binding.userImageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            binding.userImageView.setImageBitmap(bitmap)
        }
    }

    private fun uploadImage(studentId:String) {
        if (filePath != null) {
            val ref = storage.child("images/${UUID.randomUUID()}")

            val uploadTask = ref.putFile(filePath!!)

            uploadTask.addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                ref.downloadUrl.addOnSuccessListener { uri ->
                    auth.currentUser?.let { it1 ->
                        userRef.child(it1.uid).child("profile").setValue(uri.toString())
                    }

                    studentRef.child(studentId).child("profile").setValue(uri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSignUp() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val studentId = binding.etIdNumber.text.toString().trim()
        val program = binding.etProgram.text.toString().trim()
        val studentData = Student(firstName,lastName,program,studentId)

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || studentId.isEmpty()
            || program.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords do not match, please try again"
            return
        }
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    auth.currentUser?.let {
                        userRef.child(it.uid).child("firstName").setValue(firstName)
                    }
                    auth.currentUser?.let {
                        userRef.child(it.uid).child("lastName").setValue(lastName)
                    }
                    auth.currentUser?.let { userRef.child(it.uid).child("id").setValue(it.uid) }
                    auth.currentUser?.let {
                        userRef.child(it.uid).child("email").setValue(it.email)
                    }
                    auth.currentUser?.let {
                        userRef.child(it.uid).child("role").setValue("Student")
                    }
                    auth.currentUser?.let {
                        userRef.child(it.uid).child("studentId").setValue(studentId)
                    }
                    auth.currentUser?.let {
                        userRef.child(it.uid).child("program").setValue(program)
                    }

                    studentRef.child(studentId).child("studentData").setValue(studentData)

                    val intent = Intent(this, StudentDashboardActivity::class.java).apply {
                        putExtra("STUDENT_ID",studentId)
                    }
                    uploadImage(studentId)
                    startActivity(intent)

                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this, "Authentication failed: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}