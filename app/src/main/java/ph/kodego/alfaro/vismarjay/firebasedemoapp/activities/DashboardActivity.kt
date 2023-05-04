package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.R
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.CourseAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityDashboardBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.CourseModel

class DashboardActivity : AppCompatActivity(), CourseAdapter.onItemClickListener {

    private lateinit var binding:ActivityDashboardBinding
    private lateinit var courseRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var courseList: ArrayList<CourseModel>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
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

        binding.aboutImg.setOnClickListener{
            val intent = Intent(this,AboutActivity::class.java)
            startActivity(intent)
        }

        binding.btnEditProfile.setOnClickListener{
            val intent = Intent(this,EditProfileActivity::class.java)
            startActivity(intent)
        }


        if (userRef != null) {
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val firstName = snapshot.child("firstName").getValue(String::class.java)
                        val lastName = snapshot.child("lastName").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val profilePic = snapshot.child("profile").getValue(String::class.java)

                        binding.tvName.text = firstName + " $lastName"
                        binding.tvEmail.text = email

                        Glide.with(this@DashboardActivity)
                            .load(profilePic)
                            .circleCrop()
                            .into(binding.profilePic)

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
                }
            })
        }


        getCourseData()

        binding.fab.setOnClickListener{

            val intent = Intent(this,AddCourseActivity::class.java).apply {
                putExtra("INSTRUCTOR",binding.tvName.text.toString())
            }

            startActivity(intent)
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                // We don't need to support moving items up or down in this case, so we simply return false here.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val clickedCourse = courseList[position]
                val currentUser = auth.currentUser
                val database = FirebaseDatabase.getInstance()
                val courseRef = database.getReference("Course")
                val userRef = currentUser?.let { database.getReference("users").child(it.uid) }
                val builder = AlertDialog.Builder(this@DashboardActivity)
                builder.setMessage("Are you sure you want to delete ${clickedCourse.courseTitle}?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        clickedCourse.courseId?.let { userRef?.child("courses")?.child(it)?.removeValue() }
                        clickedCourse.courseId?.let { courseRef.child(it)?.removeValue() }
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
                getCourseData()
            }
        })

        itemTouchHelper.attachToRecyclerView(courseRecyclerView)

    }

    private fun getCourseData() {
        courseRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val currentUser = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val userRef = currentUser?.let { database.getReference("users").child(it.uid) }

        if (userRef != null) {
            userRef.child("courses").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    courseList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val course = snapshot.getValue(CourseModel::class.java)
                        course?.let { courseList.add(it) }
                    }
                    val adapter = CourseAdapter(courseList, this@DashboardActivity) // pass this as listener
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

    }

    override fun onItemClick(position: Int) {
        val clickedCourse = courseList[position]
        val intent = Intent(this, CourseDetailsActivity::class.java).apply {
            putExtra("COURSE_ID", clickedCourse.courseId)
            putExtra("PROGRAM", clickedCourse.program)
            putExtra("course_title", clickedCourse.courseTitle)
            putExtra("WEEKLYSCHEDULE",clickedCourse.weeklySchedule)
            putExtra("TIMESCHEDULE",clickedCourse.timeSchedule)
            putExtra("ROOMASSIGNMENT",clickedCourse.roomAssignment)
            putExtra("INSTRUCTOR",clickedCourse.instructor)
        }
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finish()
    }
}