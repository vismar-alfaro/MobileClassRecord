package ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.StudentScoreMidExam
import ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter.MidExamAdapter
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityMidExamBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EnrolledStudentModel

class MidExamActivity : AppCompatActivity(), MidExamAdapter.onItemClickListener {

    private lateinit var binding:ActivityMidExamBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentList: ArrayList<EnrolledStudentModel>
    private lateinit var auth: FirebaseAuth
    private var courseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMidExamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        courseId = intent.getStringExtra("COURSE_ID")

        auth = FirebaseAuth.getInstance()


        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        studentList = arrayListOf<EnrolledStudentModel>()

        courseId?.let { getStudentData(it) }

        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val dbRef = userId?.let {
            FirebaseDatabase.getInstance().getReference("users")
                .child(it)
                .child("courses")
                .child(courseId!!)
                .child("performanceIndicator")
                .child("midtermExam")
                .child("scoreTotal")
        }

        if (dbRef != null) {
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val totalScore = dataSnapshot.getValue(Int::class.java) ?: 0
                    binding.midExamTotalScore.text = totalScore.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error case
                    // For example, you can log the error or show an error message to the user
                    error.toException().printStackTrace()
                }
            })
        }


        binding.midExam.setOnClickListener{
            courseId?.let { it1 -> openUpdateDialog(it1) }
        }

    }

    private fun getStudentData(courseId:String) {

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
                    val adapter = MidExamAdapter(studentList, this@MidExamActivity) // pass this as listener
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


    override fun onItemClick(position: Int) {

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        val clickedStudent = studentList[position]
        val intent = Intent(this, StudentScoreMidExam::class.java).apply {
            putExtra("STUDENT_ID", clickedStudent.studentId)
            putExtra("LASTNAME", clickedStudent.lastName)
            putExtra("FIRSTNAME", clickedStudent.firstName)
            putExtra("COURSE_ID",courseId)
            putExtra("USER_ID",userId)
            putExtra("ACTIVITY_TITLE", binding.midExam.text.toString())
            putExtra("SCORE_TOTAL", binding.midExamTotalScore.text.toString().replace("Total: ", ""))
        }
        startActivity(intent)

    }

    private fun openUpdateDialog(courseId: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.add_pi_dialog,null)

        mDialog.setView(mDialogView)

        val etPiTitle = mDialogView.findViewById<EditText>(R.id.pi_title)
        val etPiId = mDialogView.findViewById<EditText>(R.id.pi_id)
        val etPiTotal = mDialogView.findViewById<EditText>(R.id.PI_total)

        val btnSave = mDialogView.findViewById<Button>(R.id.btn_save)


        mDialog.setTitle("Updating Midterm Exam")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnSave.setOnClickListener{
            val title = etPiTitle.text.toString().trim()
            val id = etPiId.text.toString().trim()
            val total = etPiTotal.text.toString().trim()

            if(title.isEmpty()||id.isEmpty()||total.isEmpty()){
                Toast.makeText(applicationContext,"Please fill all required details", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                updatePiData(
                    etPiTitle.text.toString(),
                    etPiId.text.toString(),
                    etPiTotal.text.toString().toInt(),
                    courseId
                )
                Toast.makeText(applicationContext,"$etPiTitle added", Toast.LENGTH_LONG).show()

                alertDialog.dismiss()
            }


        }

    }
    private fun updatePiData(
        etPiTitle:String,
        etPiId:String,
        etPiTotal:Int,
        courseId: String
    ){

        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val dbRef = userId?.let {
            FirebaseDatabase.getInstance().getReference("users")
                .child(it)
                .child("courses")
                .child(courseId!!)
                .child("performanceIndicator")
                .child(etPiId)
        }

        val piInfo = ActivityListModel(etPiTitle,etPiTotal,etPiId)
        if (dbRef != null) {
            dbRef.setValue(piInfo)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()

                }
        }

        binding.midExamTotalScore.text = etPiTotal.toString()
    }


}