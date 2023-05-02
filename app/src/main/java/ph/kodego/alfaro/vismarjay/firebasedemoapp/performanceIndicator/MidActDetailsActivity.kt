package ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator

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
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityMidActDetailsBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel

class MidActDetailsActivity : AppCompatActivity(), ActivityListAdapter.onItemClickListener {

    private lateinit var binding: ActivityMidActDetailsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var activityList: ArrayList<ActivityListModel>
    private lateinit var auth: FirebaseAuth
    private var courseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMidActDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent != null) {
            courseId = intent.getStringExtra("COURSE_ID")
        }

        auth = FirebaseAuth.getInstance()

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        activityList = arrayListOf<ActivityListModel>()

        getActivityData()

        binding.fab.setOnClickListener{
            openUpdateDialog()
        }


    }


    private fun getActivityData() {
        val currentUser = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val midActListRef = currentUser?.let {
            courseId?.let { it1 ->
                database.getReference("users")
                    .child(it.uid)
                    .child("courses")
                    .child(it1)
                    .child("performanceIndicator")
                    .child("midtermActivities")
                    .orderByChild("activityId")
            }
        }

        if (midActListRef != null) {
            midActListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    activityList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val course = snapshot.getValue(ActivityListModel::class.java)
                        course?.let { activityList.add(it) }
                    }
                    val adapter = ActivityListAdapter(activityList, this@MidActDetailsActivity) // pass this as listener
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
        val clickedActivity = activityList[position]
        val intent = Intent(this, MidActActivity::class.java).apply {
            putExtra("ACTIVITYID",clickedActivity.activityId)
            putExtra("SCORETOTAL",clickedActivity.scoreTotal.toString())
            putExtra("ACTIVITYTITLE",clickedActivity.activityTitle)
            putExtra("COURSE_ID",courseId)
        }
        startActivity(intent)

    }

    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog() {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.add_pi_dialog,null)

        mDialog.setView(mDialogView)

        val etPiTitle = mDialogView.findViewById<EditText>(R.id.pi_title)
        val etPiId = mDialogView.findViewById<EditText>(R.id.pi_id)
        val etPiTotal = mDialogView.findViewById<EditText>(R.id.PI_total)

        val btnSave = mDialogView.findViewById<Button>(R.id.btn_save)


        mDialog.setTitle("ADDING MIDTERM ACTIVITY")

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
                )
                Toast.makeText(applicationContext,"$etPiTitle added", Toast.LENGTH_LONG).show()

                alertDialog.dismiss()
            }


        }
    }
    private fun updatePiData(
        etPiTitle:String,
        etPiId:String,
        etPiTotal:Int
    ){

        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val dbRef = userId?.let {
            FirebaseDatabase.getInstance().getReference("users")
                .child(it)
                .child("courses")
                .child(courseId!!)
                .child("performanceIndicator")
                .child("midtermActivities")
                .child(etPiId)
        }
        val piInfo = ActivityListModel(etPiTitle,etPiTotal,etPiId)
        if (dbRef != null) {
            dbRef.setValue(piInfo)
        }
    }

}