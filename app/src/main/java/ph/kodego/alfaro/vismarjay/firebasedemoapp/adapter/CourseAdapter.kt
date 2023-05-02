package ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.DashboardActivity
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.CourseModel

class CourseAdapter(private val courseList: ArrayList<CourseModel>, private val listener: DashboardActivity):
    RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    init {
        mListener = listener
    }

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): CourseAdapter.ViewHolder{
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_list_item,parent,false)

        return ViewHolder(itemView,mListener)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CourseAdapter.ViewHolder, position:Int){
        val course = courseList[position]
        holder.courseTitle.text = course.courseTitle
        holder.program.text = course.program
        holder.weeklySchedule.text = course.weeklySchedule
        holder.timeSchedule.text = course.timeSchedule
        holder.roomAssignment.text = course.roomAssignment
        holder.instructor.text = "Instructor: ${course.instructor}"
    }


    override fun getItemCount(): Int {
        return courseList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val courseTitle: TextView = itemView.findViewById(R.id.course_title)
        val program: TextView = itemView.findViewById(R.id.program)
        val weeklySchedule: TextView = itemView.findViewById(R.id.weekly_schedule)
        val timeSchedule: TextView = itemView.findViewById(R.id.time_schedule)
        val roomAssignment: TextView = itemView.findViewById(R.id.room_assignment)
        val instructor: TextView = itemView.findViewById(R.id.instructor)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}