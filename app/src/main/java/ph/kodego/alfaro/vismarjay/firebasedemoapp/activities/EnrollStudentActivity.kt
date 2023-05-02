package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityEnrollStudentBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EnrolledStudentModel

class EnrollStudentActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEnrollStudentBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var courseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Course")

        val intent = intent
        if (intent != null) {
            courseId = intent.getStringExtra("COURSE_ID")
        }
        val courseTitle = intent.getStringExtra("COURSE_TITLE")
        val instructor = intent.getStringExtra("INSTRUCTOR")
        val weeklySchedule = intent.getStringExtra("WEEKLYSCHEDULE")
        val timeSchedule = intent.getStringExtra("TIMESCHEDULE")
        val roomAssignment = intent.getStringExtra("ROOMASSIGNMENT")

        binding.saveButton.setOnClickListener {
            val title = courseTitle ?: ""
            val instructorName = instructor ?: ""
            val weeklySched = weeklySchedule ?: ""
            val timeSched = timeSchedule ?: ""
            val roomAssign = roomAssignment ?: ""
            saveStudentData(title, instructorName, weeklySched, timeSched, roomAssign)
        }
    }

    private fun saveStudentData(courseTitle:String,
                                instructor:String,
                                weeklySchedule:String,
                                timeSchedule:String,
                                roomAssignment:String)
    {
        val studentId = binding.studentID.text.toString()
        val lastName = binding.studentLastName.text.toString()
        val firstName = binding.studentFirstName.text.toString()
        val program = binding.program.text.toString()
        val currentUser = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val userRef = currentUser?.let { database.getReference("users").child(it.uid) }
        val enstudentRef = courseId?.let { userRef?.child("courses")?.child(it)}
        val student = EnrolledStudentModel(studentId,lastName,firstName,program)
        val studentRef = database.getReference("Students")
        val enrolledCourse = courseId?.let {
            database.getReference("Students").child(studentId)
                .child("coursesEnrolled")
                .child(it)
        }


        if (studentId.isEmpty()) {
            binding.studentID.error = "Please enter Student ID"
            return
        }
        if (lastName.isEmpty()) {
            binding.studentLastName.error = "Please enter Student LastName"
            return
        }
        if (firstName.isEmpty()) {
            binding.studentFirstName.error = "Please enter Student FirstName"
            return
        }
        if (program.isEmpty()) {
            binding.program.error = "Please enter Program"
            return
        }

        if (enstudentRef != null) {
            enstudentRef.child("students").child(studentId).setValue(student)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()

                }
        }
        studentRef.child(studentId).setValue(student)
            ?.addOnCompleteListener {
                Toast.makeText(this, "${firstName + lastName} inserted successfully", Toast.LENGTH_SHORT).show()

                binding.studentID.text.clear()
                binding.studentLastName.text.clear()
                binding.studentFirstName.text.clear()
                binding.program.text.clear()

            }?.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()

            }

        courseId?.let { studentRef.child(studentId).child("coursesEnrolled").child(it)
            .child("courseId").setValue(courseId)}
        courseId?.let { studentRef.child(studentId).child("coursesEnrolled").child(it)
            .child("instructor").setValue(instructor)}
        courseId?.let { studentRef.child(studentId).child("coursesEnrolled").child(it)
            .child("weeklySchedule").setValue(weeklySchedule)}
        courseId?.let { studentRef.child(studentId).child("coursesEnrolled").child(it)
            .child("timeSchedule").setValue(timeSchedule)}
        courseId?.let { studentRef.child(studentId).child("coursesEnrolled").child(it)
            .child("roomAssignment").setValue(roomAssignment)}
        courseId?.let { studentRef.child(studentId).child("coursesEnrolled").child(it)
            .child("courseTitle").setValue(courseTitle)}
            ?.addOnCompleteListener{
                Toast.makeText(this,"$courseTitle inserted successfully",Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener{err->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }

    }
}