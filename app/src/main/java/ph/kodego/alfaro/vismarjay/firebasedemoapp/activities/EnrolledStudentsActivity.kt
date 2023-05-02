package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.CourseAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.EnrolledStudentAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityEnrolledStudentsBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.CourseModel
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EnrolledStudentModel

class EnrolledStudentsActivity : AppCompatActivity(), EnrolledStudentAdapter.onItemClickListener {

    private lateinit var binding: ActivityEnrolledStudentsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var studentList: ArrayList<EnrolledStudentModel>
    private lateinit var auth: FirebaseAuth
    private var courseId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrolledStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courseDesc = intent.getStringExtra("course_title")
        val program = intent.getStringExtra("PROGRAM")
        val instructor = intent.getStringExtra("INSTRUCTOR")
        val weeklySchedule = intent.getStringExtra("WEEKLYSCHEDULE")
        val timeSchedule = intent.getStringExtra("TIMESCHEDULE")
        val roomAssignment = intent.getStringExtra("ROOMASSIGNMENT")
        val intent = intent
        if (intent != null) {
            courseId = intent.getStringExtra("COURSE_ID")
        }

        auth = FirebaseAuth.getInstance()


        recyclerView = binding.enrolledStudentListRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        tvLoadingData = binding.tvLoadingData

        studentList = arrayListOf<EnrolledStudentModel>()

        getStudentData()

        binding.fab.setOnClickListener{

            val intent = Intent(this,EnrollStudentActivity::class.java).apply{
                putExtra("COURSE_ID", courseId )
                putExtra("COURSE_TITLE",courseDesc)
                putExtra("INSTRUCTOR",instructor)
                putExtra("WEEKLYSCHEDULE",weeklySchedule)
                putExtra("TIMESCHEDULE",timeSchedule)
                putExtra("ROOMASSIGNMENT",roomAssignment)

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
                val clickedStudent = studentList[position]
                val currentUser = auth.currentUser
                val database = FirebaseDatabase.getInstance()
                val studentRef = database.getReference("Students")
                val enstudentRef = currentUser?.let {
                    courseId?.let { it1 ->
                        database.getReference("users").child(it.uid).child("courses")
                            .child(it1).child("students")
                    }
                }

                val builder = AlertDialog.Builder(this@EnrolledStudentsActivity)
                builder.setMessage("Are you sure you want to delete ${clickedStudent.firstName} ${clickedStudent.lastName}?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        clickedStudent.studentId?.let { enstudentRef?.child(it)?.removeValue() }
                        clickedStudent.studentId?.let { courseId?.let { it1 ->
                            studentRef.child(it)?.child(
                                it1
                            )?.removeValue()
                        } }
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
                getStudentData()
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)


    }

    private fun getStudentData() {
        recyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

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
                    val adapter = EnrolledStudentAdapter(studentList, this@EnrolledStudentsActivity) // pass this as listener
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
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
        val clickedStudent = studentList[position]
        val intent = Intent(this, StudentDetailsActivity::class.java).apply {
            putExtra("STUDENT_ID", clickedStudent.studentId)
            putExtra("LASTNAME", clickedStudent.lastName)
            putExtra("FIRSTNAME", clickedStudent.firstName)
            putExtra("COURSE_ID",courseId)

        }
        startActivity(intent)

    }
}