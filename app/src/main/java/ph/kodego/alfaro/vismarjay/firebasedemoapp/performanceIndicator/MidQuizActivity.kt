package ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.StudentScore
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.StudentScoreMidQuiz
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.MidActAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.MidQuizAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityMidQuizBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EnrolledStudentModel

class MidQuizActivity : AppCompatActivity(), MidQuizAdapter.onItemClickListener {

    private lateinit var binding:ActivityMidQuizBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentList: ArrayList<EnrolledStudentModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var courseId: String? = null
    private var activityId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMidQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityTitle = intent.getStringExtra("ACTIVITYTITLE")
        val scoreTotal = intent.getStringExtra("SCORETOTAL")
        val intent = intent
        if (intent != null) {
            activityId = intent.getStringExtra("ACTIVITYID")
        }
        if (intent != null) {
            courseId = intent.getStringExtra("COURSE_ID")
        }

        auth = FirebaseAuth.getInstance()


        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        studentList = arrayListOf<EnrolledStudentModel>()

        binding.midQuizNo.text = activityTitle
        binding.quizNoTotalScore.text = "Total: $scoreTotal"

        getStudentData()

    }

    private fun getStudentData() {

        val currentUser = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val enstudentRef = currentUser?.let {
            courseId?.let { it1 ->
                database.getReference("users").child(it.uid).child("courses")
                    .child(it1).child("students").orderByChild("lastName")
            }
        }

        if (enstudentRef != null) {
            enstudentRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    studentList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val course = snapshot.getValue(EnrolledStudentModel::class.java)
                        course?.let { studentList.add(it) }
                    }
                    val adapter = MidQuizAdapter(studentList, this@MidQuizActivity) // pass this as listener
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error case
                    // For example, you can log the error or show an error message to the user
                    error.toException().printStackTrace()
                }
            })
        }


    }


    override fun onItemClick(position: Int) {

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        val clickedStudent = studentList[position]
        val intent = Intent(this, StudentScoreMidQuiz::class.java).apply {
            putExtra("STUDENT_ID", clickedStudent.studentId)
            putExtra("LASTNAME", clickedStudent.lastName)
            putExtra("FIRSTNAME", clickedStudent.firstName)
            putExtra("ACTIVITYID",activityId)
            putExtra("COURSE_ID",courseId)
            putExtra("USER_ID",userId)
            putExtra("ACTIVITY_TITLE", binding.midQuizNo.text.toString())
            putExtra("SCORE_TOTAL", binding.quizNoTotalScore.text.toString().replace("Total: ", ""))
        }
        startActivity(intent)

    }

}