package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.StudentActivitiesAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.StudentFinActivitiesAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityStudentFinActivitiesBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel

class StudentFinActivitiesActivity : AppCompatActivity() {

    private lateinit var binding:ActivityStudentFinActivitiesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var activityList: ArrayList<ActivityListModel>
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentFinActivitiesBinding.inflate(layoutInflater)
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
        val database = FirebaseDatabase.getInstance()
        val finActListRef = database.getReference("Students").child(studentId)
            .child("coursesEnrolled")
            .child(courseId).child("finalActivities")
            .orderByChild("activityId")

        if (finActListRef != null) {
            finActListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    activityList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val course = snapshot.getValue(ActivityListModel::class.java)
                        course?.let { activityList.add(it) }
                    }
                    val adapter = StudentFinActivitiesAdapter(activityList, this@StudentFinActivitiesActivity) // pass this as listener
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