package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityStudentDetailsBinding

class StudentCourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        val courseId = intent.getStringExtra("COURSE_ID")
        val studentId = intent.getStringExtra("STUDENT_ID")
        val studentRef = db.getReference("Students").child(studentId.toString())
        val studentCourseRef = studentRef.child("coursesEnrolled").child(courseId.toString())

        studentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profilePic = dataSnapshot.child("profile").value.toString()
                val lastName = dataSnapshot.child("lastName").value.toString()
                val firstName = dataSnapshot.child("firstName").value.toString()
                val studentId = dataSnapshot.child("studentId").value.toString()

                Glide.with(this@StudentCourseActivity)
                    .load(profilePic)
                    .circleCrop()
                    .into(binding.profilePic)

                binding.tvLastName.text = "$lastName"
                binding.tvFirstName.text = "$firstName"
                binding.tvIdnumber.text = "$studentId"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled", error.toException())
            }
        })

        studentCourseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val midActScore = dataSnapshot.child("midActScore").value.toString()
                val midActTotalScore =
                    dataSnapshot.child("midActTotalScore").value.toString()
                val midQuizScore = dataSnapshot.child("midQuizScore").value.toString()
                val midQuizTotalScore =
                    dataSnapshot.child("midQuizTotalScore").value.toString()
                val midExamScore = dataSnapshot.child("midExamScore").value.toString()
                val midExamTotalScore =
                    dataSnapshot.child("midExamTotalScore").value.toString()
                val midPitScore = dataSnapshot.child("midPitScore").value.toString()
                val midPitTotalScore =
                    dataSnapshot.child("midPitTotalScore").value.toString()
                val finActScore = dataSnapshot.child("finActScore").value.toString()
                val finActTotalScore =
                    dataSnapshot.child("finActTotalScore").value.toString()
                val finQuizScore = dataSnapshot.child("finQuizScore").value.toString()
                val finQuizTotalScore =
                    dataSnapshot.child("finQuizTotalScore").value.toString()
                val finExamScore = dataSnapshot.child("finExamScore").value.toString()
                val finExamTotalScore =
                    dataSnapshot.child("finExamTotalScore").value.toString()
                val finPitScore = dataSnapshot.child("finPitScore").value.toString()
                val finPitTotalScore =
                    dataSnapshot.child("finPitTotalScore").value.toString()
                val midtermGrade = dataSnapshot.child("midtermGrade").value.toString().toDouble()
                val finalGrade = dataSnapshot.child("finalGrade").value ?: 0.0

                binding.midActScore.text = "$midActScore/$midActTotalScore"
                binding.midQuizScore.text = "$midQuizScore/$midQuizTotalScore"
                binding.midExamScore.text = "$midExamScore/$midExamTotalScore"
                binding.midPitScore.text = "$midPitScore/$midPitTotalScore"
                binding.tvMidRating.text = "$midtermGrade"
                binding.finActScore.text = "$finActScore/$finActTotalScore"
                binding.finQuizScore.text = "$finQuizScore/$finQuizTotalScore"
                binding.finExamScore.text = "$finExamScore/$finExamTotalScore"
                binding.finPitScore.text = "$finPitScore/$finPitTotalScore"
                binding.tvFinRating.text = "$finalGrade"



            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled", error.toException())
            }
        })


    }

}