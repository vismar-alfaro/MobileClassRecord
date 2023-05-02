package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityStudentDetailsBinding
import java.util.logging.Level.INFO

class StudentDetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityStudentDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseDatabase
    var data1:Double? = null
    var data2:Double? = null
    var data3:Double? = null
    var data4:Double? = null
    var finData1:Double? = null
    var finData2:Double? = null
    var finData3:Double? = null
    var finData4:Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        val studentId = intent.getStringExtra("STUDENT_ID")
        val lastName = intent.getStringExtra("LASTNAME")
        val firstName = intent.getStringExtra("FIRSTNAME")
        val courseId = intent.getStringExtra("COURSE_ID")
        val studentRef = db.getReference("Students").child(studentId?:"")

//        binding.tvLastName.text = lastName ?: ""
//        binding.tvFirstName.text = firstName ?: ""
//        binding.tvIdnumber.text = studentId ?: ""

        val gradeRef = studentRef.child("coursesEnrolled").child(courseId?:"")

        val midActRef = gradeRef.child("midtermActivities")
        val midQuizRef = gradeRef.child("midtermQuizzes")
        val midExamRef = gradeRef.child("midtermExam")
        val midPitRef = gradeRef.child("midtermPit")
        val finActRef = gradeRef.child("finalActivities")
        val finQuizRef = gradeRef.child("finalQuizzes")
        val finExamRef = gradeRef.child("finalExam")
        val finPitRef = gradeRef.child("finalPit")

        var midQuizTotalScore = 0
        var midQuizScore = 0
        var midActTotalScore = 0
        var midActScore = 0

        var finActTotalScore = 0
        var finActScore = 0
        var finQuizTotalScore = 0
        var finQuizScore = 0

        studentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val profilePic = dataSnapshot.child("profile").value.toString()
                    val lastName = dataSnapshot.child("lastName").value.toString()
                    val firstName = dataSnapshot.child("firstName").value.toString()
                    val studentId = dataSnapshot.child("studentId").value.toString()

                    Glide.with(this@StudentDetailsActivity)
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


        midActRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (activitySnapshot in dataSnapshot.children) {
                    val scoreTotal = activitySnapshot.child("scoreTotal").getValue(Int::class.java) ?: 0
                    midActTotalScore += scoreTotal
                    val scoreAct = activitySnapshot.child("score").getValue(Int::class.java) ?: 0
                    midActScore += scoreAct
                    data1 = midActScore.toDouble()/midActTotalScore.toDouble()
                }
                binding.midActScore.text = "$midActScore/$midActTotalScore"
                gradeRef.child("midActTotalScore").setValue(midActTotalScore)
                gradeRef.child("midActScore").setValue(midActScore)
                calculateResult(gradeRef)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
            }
        })

        midQuizRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (activitySnapshot in dataSnapshot.children) {
                    val scoreTotal = activitySnapshot.child("scoreTotal").getValue(Int::class.java) ?: 0
                    midQuizTotalScore += scoreTotal
                    val scoreAct = activitySnapshot.child("score").getValue(Int::class.java) ?: 0
                    midQuizScore += scoreAct
                    data2 = midQuizScore.toDouble()/midQuizTotalScore.toDouble()
                }
                binding.midQuizScore.text = "$midQuizScore/$midQuizTotalScore"
                gradeRef.child("midQuizTotalScore").setValue(midQuizTotalScore)
                gradeRef.child("midQuizScore").setValue(midQuizScore)
                calculateResult(gradeRef)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
            }
        })

        midExamRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val midtermData = snapshot.value as HashMap<*, *>?
                val score = midtermData?.get("score")
                val scoreTotal = midtermData?.get("scoreTotal")

                if(score != null && scoreTotal != null){
                    data3 = score.toString().toDouble()/scoreTotal.toString().toDouble()
                }else{
                    data3 = 0.0
                }


                binding.midExamScore.text = "${score?:0}/${scoreTotal?:0}"
                gradeRef.child("midExamTotalScore").setValue(scoreTotal?:0)
                gradeRef.child("midExamScore").setValue(score?:0)
                calculateResult(gradeRef)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        midPitRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val midtermData = snapshot.value as HashMap<*, *>?
                val score = midtermData?.get("score")
                val scoreTotal = midtermData?.get("scoreTotal")

                if(score != null && scoreTotal != null){
                    data4 = score.toString().toDouble()/scoreTotal.toString().toDouble()
                }else{
                    data4 = 0.0
                }


                binding.midPitScore.text = "${score?:0}/${scoreTotal?:0}"
                gradeRef.child("midPitTotalScore").setValue(scoreTotal?:0)
                gradeRef.child("midPitScore").setValue(score?:0)
                calculateResult(gradeRef)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })



        finActRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (activitySnapshot in dataSnapshot.children) {
                    val scoreTotal = activitySnapshot.child("scoreTotal").getValue(Int::class.java) ?: 0
                    finActTotalScore += scoreTotal
                    val scoreAct = activitySnapshot.child("score").getValue(Int::class.java) ?: 0
                    finActScore += scoreAct

                    finData1 = finActScore/finActTotalScore.toDouble()
                }
                binding.finActScore.text = "$finActScore/$finActTotalScore"
                gradeRef.child("finActTotalScore").setValue(finActTotalScore)
                gradeRef.child("finActScore").setValue(finActScore)
                calculateResultFin(gradeRef)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
            }
        })

        finQuizRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (activitySnapshot in dataSnapshot.children) {
                    val scoreTotal = activitySnapshot.child("scoreTotal").getValue(Int::class.java) ?: 0
                    finQuizTotalScore += scoreTotal
                    val scoreAct = activitySnapshot.child("score").getValue(Int::class.java) ?: 0
                    finQuizScore += scoreAct

                    finData2 = finQuizScore/finQuizTotalScore.toDouble()
                }
                binding.finQuizScore.text = "$finQuizScore/$finQuizTotalScore"
                gradeRef.child("finQuizTotalScore").setValue(finQuizTotalScore)
                gradeRef.child("finQuizScore").setValue(finQuizScore)

                calculateResultFin(gradeRef)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
            }
        })

        finExamRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val finalTermData = snapshot.value as HashMap<*, *>?
                val score = finalTermData?.get("score")
                val scoreTotal = finalTermData?.get("scoreTotal")

                if(score != null && scoreTotal != null){
                    finData3 = score.toString().toDouble()/scoreTotal.toString().toDouble()
                }else{
                    finData3 = 0.0
                }

                binding.finExamScore.text = "${score?:0}/${scoreTotal?:0}"
                gradeRef.child("finExamTotalScore").setValue(scoreTotal?:0)
                gradeRef.child("finExamScore").setValue(score?:0)

                calculateResultFin(gradeRef)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        finPitRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val finalTermData = snapshot.value as HashMap<*, *>?
                val score = finalTermData?.get("score")
                val scoreTotal = finalTermData?.get("scoreTotal")

                if(score != null && scoreTotal != null){
                    finData4 = score.toString().toDouble()/scoreTotal.toString().toDouble()
                }else{
                    finData4 = 0.0
                }


                binding.finPitScore.text = "${score?:0}/${scoreTotal?:0}"
                gradeRef.child("finPitTotalScore").setValue(scoreTotal?:0)
                gradeRef.child("finPitScore").setValue(score?:0)

                calculateResultFin(gradeRef)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    private fun calculateResult(gradeRef:DatabaseReference){
        var resultAve:Double? = null
        if(data1 != null && data2 != null && data3 != null && data4 != null){
            val result = (data1!! * 0.1) + (data2!! * 0.4) + (data3!! * 0.3) + (data4!! * 0.2)
            Log.i("StudentDetailsActivity","Act = $data1,Quiz = $data2, Exam = $data3, Pit=$data4")
            if(result>=0.7){
                resultAve = (23/3)-((20/3)*result)
                val gradeRate = rating(resultAve)
                binding.tvMidRating.text = gradeRate.toString()
                gradeRef.child("midtermGrade").setValue(gradeRate)

            }else{
                resultAve = 5-((20/7)*result)
                rating(resultAve)
                val gradeRate = rating(resultAve)
                binding.tvMidRating.text = gradeRate.toString()
                gradeRef.child("midtermGrade").setValue(gradeRate)

            }
        }

    }

    private fun calculateResultFin(gradeRef: DatabaseReference){
        var resultAve:Double? = null
        if(finData1 != null && finData2 != null && finData3 != null && finData4 != null){
            val result = (finData1!! * 0.1) + (finData2!! * 0.4) + (finData3!! * 0.3) + (finData4!! * 0.2)
            if(result>=0.7){
                resultAve = (23/3)-((20/3)*result)
                val gradeRate = rating(resultAve)
                binding.tvFinRating.text = gradeRate.toString()
                gradeRef.child("finalGrade").setValue(gradeRate)

            }else{
                resultAve = 5-((20/7)*result)
                rating(resultAve)
                val gradeRate = rating(resultAve)
                binding.tvFinRating.text = gradeRate.toString()
                gradeRef.child("finalGrade").setValue(gradeRate)

            }
        }

    }


    private fun rating(resultAve:Double):Double{

        return when{
            resultAve <= 1.124 -> 1.0
            resultAve <= 1.374 -> 1.25
            resultAve <= 1.624 -> 1.5
            resultAve <= 1.874 -> 1.75
            resultAve <= 2.124 -> 2.0
            resultAve <= 2.374 -> 2.25
            resultAve <= 2.624 -> 2.5
            resultAve <= 2.874 -> 2.75
            resultAve <= 3.124 -> 3.0
            resultAve <= 3.374 -> 3.25
            resultAve <= 3.624 -> 3.5
            resultAve <= 3.874 -> 3.75
            resultAve <= 4.124 -> 4.0
            resultAve <= 4.374 -> 4.25
            resultAve <= 4.624 -> 4.5
            resultAve <= 4.874 -> 4.75
            resultAve <= 5.124 -> 5.0
            else -> {5.0}
        }

    }


}

