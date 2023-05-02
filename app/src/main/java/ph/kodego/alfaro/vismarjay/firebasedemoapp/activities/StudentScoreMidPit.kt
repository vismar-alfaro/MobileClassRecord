package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityStudentScoreBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel

class StudentScoreMidPit: AppCompatActivity() {

    private lateinit var binding: ActivityStudentScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lastName = intent.getStringExtra("LASTNAME")
        val firsName = intent.getStringExtra("FIRSTNAME")
        val scoreTotal = intent.getStringExtra("SCORE_TOTAL").toString()
        val activityTitle = intent.getStringExtra("ACTIVITY_TITLE").toString()
        val studentId = intent.getStringExtra("STUDENT_ID")
        val courseId = intent.getStringExtra("COURSE_ID")
        val userId = intent.getStringExtra("USER_ID").toString()
        val database = FirebaseDatabase.getInstance()
        val enstudentRef = database.getReference("users")
            .child(userId)
            .child("courses")
            .child(courseId!!)
            .child("students")
            .child(studentId!!)
            .child("midtermPit")
        val studentRef = database.getReference("Students")
            .child(studentId!!)
            .child("coursesEnrolled")
            .child(courseId!!)
            .child("midtermPit")


        binding.studName.text = "$firsName $lastName"
        binding.PI.text = "$activityTitle"
        binding.PITotal.text = "$scoreTotal"

        binding.btnSave.setOnClickListener{

            val score = binding.studScore.text.toString()

            if (score.isEmpty()) {
                binding.studScore.error = "Please enter Score"
                return@setOnClickListener
            }

            val actScoreDetail = ActivityListModel(activityTitle,scoreTotal.toInt(),"midtermPit",score.toInt())
            enstudentRef.setValue(actScoreDetail)
                .addOnCompleteListener {
                    Toast.makeText(this, "$score out ot $scoreTotal", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Inserted successfully", Toast.LENGTH_SHORT).show()
                    binding.studScore.text.clear()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()

                }
            studentRef.setValue(actScoreDetail)

        }

        enstudentRef.child("score").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val score = dataSnapshot.getValue(Int::class.java) ?: 0
                binding.PIScore.text = "Score: $score"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error case
                // For example, you can log the error or show an error message to the user
                error.toException().printStackTrace()
            }
        })



    }

    fun toast(string1:String,string2: String){
        Toast.makeText(this,"Score is $string1 out of $string2", Toast.LENGTH_SHORT).show()

    }
}