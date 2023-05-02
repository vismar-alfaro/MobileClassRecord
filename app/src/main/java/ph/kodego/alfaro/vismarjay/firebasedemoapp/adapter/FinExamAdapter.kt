package ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EnrolledStudentModel
import ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator.FinExamActivity
import ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator.MidExamActivity

class FinExamAdapter (private val enrolledStudentList: ArrayList<EnrolledStudentModel>, private val listener: FinExamActivity):
    RecyclerView.Adapter<FinExamAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): FinExamAdapter.ViewHolder{
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stud_score_item,parent,false)

        return ViewHolder(itemView,mListener)

    }

    override fun onBindViewHolder(holder: FinExamAdapter.ViewHolder, position:Int){
        val student = enrolledStudentList[position]
        holder.fullName.text = "${student.lastName}, ${student.firstName}"
    }


    override fun getItemCount(): Int {
        return enrolledStudentList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val fullName: TextView = itemView.findViewById(R.id.full_name)


        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }
}