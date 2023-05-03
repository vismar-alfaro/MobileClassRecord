package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.databinding.ActivityCourseDetailsBinding
import ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator.*

class CourseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courseDesc = intent.getStringExtra("course_title")
        val program = intent.getStringExtra("PROGRAM")
        val courseId = intent.getStringExtra("COURSE_ID")
        val weeklySchedule = intent.getStringExtra("WEEKLYSCHEDULE")
        val timeSchedule = intent.getStringExtra("TIMESCHEDULE")
        val roomAssignment = intent.getStringExtra("ROOMASSIGNMENT")
        val instructor = intent.getStringExtra("INSTRUCTOR")

        binding.courseTitle.text = courseDesc
        binding.program.text = program



        binding.cvStudent.setOnClickListener{
            val intent = Intent(this,EnrolledStudentsActivity::class.java).apply{
            putExtra("PROGRAM", program)
            putExtra("COURSE_ID", courseId )
            putExtra("course_title", courseDesc)
            putExtra("WEEKLYSCHEDULE",weeklySchedule)
            putExtra("TIMESCHEDULE",timeSchedule)
            putExtra("ROOMASSIGNMENT",roomAssignment)
            putExtra("INSTRUCTOR", instructor)
            }
            startActivity(intent)
        }

        binding.cvMidActivities.setOnClickListener{
            val intent = Intent(this,MidActDetailsActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

        binding.cvMidQuiz.setOnClickListener{
            val intent = Intent(this,MidQuizDetailsActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

        binding.cvMidExam.setOnClickListener{
            val intent = Intent(this, MidExamActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

        binding.cvMidPit.setOnClickListener{
            val intent = Intent(this, MidPitActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

        binding.cvFinActivities.setOnClickListener{
            val intent = Intent(this,FinActDetailsActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

        binding.cvFinQuiz.setOnClickListener{
            val intent = Intent(this,FinQuizDetailsActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

        binding.cvFinExam.setOnClickListener{
            val intent = Intent(this, FinExamActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

        binding.cvFinPit.setOnClickListener{
            val intent = Intent(this, FinPitActivity::class.java).apply{
                putExtra("PROGRAM", program)
                putExtra("COURSE_ID", courseId )
                putExtra("course_title", courseDesc)}
            startActivity(intent)
        }

    }

}