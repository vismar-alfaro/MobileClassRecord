package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityAddCourseBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.CourseModel
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EmployeeModel

class AddCourseActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddCourseBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance().getReference("Course")

        val intent = intent.getStringExtra("INSTRUCTOR")

        binding.saveButton.setOnClickListener {
            saveCourseData(intent?:"")

        }
    }

    private fun saveCourseData(intent:String) {
        val courseTitle = binding.courseTitle.text.toString()
        val program = binding.program.text.toString()
        val weeklySchedule = binding.weeklySchedule.text.toString()
        val timeSchedule = binding.timeSchedule.text.toString()
        val roomAssignment = binding.roomAssignment.text.toString()
        val currentUser = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val userRef = currentUser?.let { database.getReference("users").child(it.uid) }


        if (courseTitle.isEmpty()) {
            binding.courseTitle.error = "Please enter Course Description"
            return
        }
        if (program.isEmpty()) {
            binding.program.error = "Please enter Program"
            return
        }
        if (weeklySchedule.isEmpty()) {
            binding.weeklySchedule.error = "Please enter Weekly Schedule"
            return
        }
        if (timeSchedule.isEmpty()) {
            binding.timeSchedule.error = "Please enter Time Schedule"
            return
        }
        if (roomAssignment.isEmpty()) {
            binding.roomAssignment.error = "Please enter Room Assignment"
            return
        }


        val courseId = dbRef.push().key!!

        val course = CourseModel(courseId, courseTitle, program, weeklySchedule,timeSchedule,roomAssignment,intent)

        if (userRef != null) {
            userRef.child("courses").child(courseId).setValue(course)
        }
        dbRef.child(courseId).setValue(course)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                binding.courseTitle.text.clear()
                binding.program.text.clear()
                binding.weeklySchedule.text.clear()
                binding.timeSchedule.text.clear()
                binding.roomAssignment.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }



    }

}
