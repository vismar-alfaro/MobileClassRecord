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
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityEditProfileBinding
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var studentRef: DatabaseReference
    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private val storage = FirebaseStorage.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        database = Firebase.database
        userRef = database.getReference("users")
        auth = Firebase.auth

        binding.btnUploadimg.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }

        binding.buttonSave.setOnClickListener {
            performSave()
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

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storage.child("images/${UUID.randomUUID()}")

            val uploadTask = ref.putFile(filePath!!)

            uploadTask.addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                ref.downloadUrl.addOnSuccessListener { uri ->
                    auth.currentUser?.let { it1 ->
                        userRef.child(it1.uid).child("profile").setValue(uri.toString())
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSave() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        auth.currentUser?.let {
            userRef.child(it.uid).child("firstName").setValue(firstName)
        }
        auth.currentUser?.let {
            userRef.child(it.uid).child("lastName").setValue(lastName)
        }

        val intent = Intent(this, DashboardActivity::class.java)
        uploadImage()
        startActivity(intent)

        Toast.makeText(this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show()
    }


}