package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.CourseAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.StudentCourseAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityStudentDashboardBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.CourseModel

class StudentDashboardActivity : AppCompatActivity(), StudentCourseAdapter.onItemClickListener {

    private lateinit var binding: ActivityStudentDashboardBinding
    private lateinit var courseRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var courseList: ArrayList<CourseModel>
    private lateinit var auth: FirebaseAuth
    var studentId:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").orderByChild("id").equalTo(currentUser?.uid)

        courseRecyclerView = binding.courseRecyclerview
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.setHasFixedSize(true)
        tvLoadingData = binding.tvLoadingData

        courseList = arrayListOf<CourseModel>()


        if (userRef != null) {
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val firstName = snapshot.child("firstName").getValue(String::class.java)
                        val lastName = snapshot.child("lastName").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val profilePic = snapshot.child("profile").getValue(String::class.java)
                        studentId = snapshot.child("studentId").getValue(String::class.java)

                        binding.tvName.text = firstName + " $lastName"
                        binding.tvEmail.text = email
                        binding.studentId.text = studentId

                        Glide.with(this@StudentDashboardActivity)
                            .load(profilePic)
                            .circleCrop()
                            .into(binding.profilePic)

                        getCourseData(studentId?:"")

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
                }
            })
        }

    }

    private fun getCourseData(studentId:String) {
        courseRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val database = FirebaseDatabase.getInstance()
        val studCourseRef = database.getReference("Students").child(studentId?:"")
            .child("coursesEnrolled")

        studCourseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                courseList.clear()
                for (snapshot in dataSnapshot.children) {
                    val course = snapshot.getValue(CourseModel::class.java)
                    course?.let { courseList.add(it) }
                }
                val adapter = StudentCourseAdapter(courseList,this@StudentDashboardActivity) // pass this as listener
                courseRecyclerView.adapter = adapter
                courseRecyclerView.visibility = View.VISIBLE
                tvLoadingData.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error case
                // For example, you can log the error or show an error message to the user
                error.toException().printStackTrace()
            }
        })

    }

    override fun onItemClick(position: Int) {
        val clickedCourse = courseList[position]
        val intent = Intent(this, StudentCourseActivity::class.java).apply {
            putExtra("COURSE_ID", clickedCourse.courseId)
            putExtra("PROGRAM", clickedCourse.program)
            putExtra("course_title", clickedCourse.courseTitle)
            putExtra("INSTRUCTOR", clickedCourse.instructor)
            putExtra("STUDENT_ID",studentId)
        }
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finish()
    }

}