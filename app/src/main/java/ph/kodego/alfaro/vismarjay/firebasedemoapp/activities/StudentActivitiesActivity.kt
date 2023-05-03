package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.ActivityListAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.StudentActivitiesAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityStudentActivitiesBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.Student
import ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator.MidActActivity

class StudentActivitiesActivity : AppCompatActivity(){

    private lateinit var binding:ActivityStudentActivitiesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var activityList: ArrayList<ActivityListModel>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val studentId = intent.getStringExtra("STUDENT_ID")
        val courseId = intent.getStringExtra("COURSE_ID")

        auth = FirebaseAuth.getInstance()

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        activityList = arrayListOf<ActivityListModel>()

        getActivityData(studentId?:"",courseId?:"")


    }


    private fun getActivityData(studentId:String,courseId:String) {
        val currentUser = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val midActListRef = database.getReference("Students").child(studentId)
            .child("coursesEnrolled")
            .child(courseId).child("midtermActivities")
            .orderByChild("activityId")

        if (midActListRef != null) {
            midActListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    activityList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val course = snapshot.getValue(ActivityListModel::class.java)
                        course?.let { activityList.add(it) }
                    }
                    val adapter = StudentActivitiesAdapter(activityList, this@StudentActivitiesActivity) // pass this as listener
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error case
                    // For example, you can log the error or show an error message to the user
                    error.toException().printStackTrace()
                }
            })
        }
    }

}