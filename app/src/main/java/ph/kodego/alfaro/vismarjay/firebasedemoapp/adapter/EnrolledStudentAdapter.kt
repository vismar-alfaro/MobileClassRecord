package ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.EnrolledStudentsActivity
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EnrolledStudentModel

class EnrolledStudentAdapter (private val enrolledStudentList: ArrayList<EnrolledStudentModel>, private val listener: EnrolledStudentsActivity):
    RecyclerView.Adapter<EnrolledStudentAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): EnrolledStudentAdapter.ViewHolder{
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item,parent,false)

        return ViewHolder(itemView,mListener)

    }

    override fun onBindViewHolder(holder: EnrolledStudentAdapter.ViewHolder, position:Int){
        val student = enrolledStudentList[position]
        holder.lastName.text = student.lastName
        holder.program.text = student.program
        holder.firstName.text = student.firstName
        holder.studentId.text = student.studentId
    }


    override fun getItemCount(): Int {
        return enrolledStudentList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val lastName: TextView = itemView.findViewById(R.id.last_name)
        val program: TextView = itemView.findViewById(R.id.program)
        val firstName: TextView = itemView.findViewById(R.id.first_name)
        val studentId: TextView = itemView.findViewById(R.id.id)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}