package ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.StudentFinQuizActivity
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.StudentMidQuizActivity
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel

class StudentFinQuizAdapter(private val activityList: ArrayList<ActivityListModel>, private val listener: StudentFinQuizActivity):
    RecyclerView.Adapter<StudentFinQuizAdapter.ViewHolder>() {


    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentFinQuizAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.perf_ind_item, parent, false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: StudentFinQuizAdapter.ViewHolder, position: Int) {
        val activity = activityList[position]
        holder.activityNo.text = activity.activityTitle.toString()
        holder.scoreTotal.text = "${activity.scoreTotal.toString()}/${activity.score.toString()}"
    }


    override fun getItemCount(): Int {
        return activityList.size
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val activityNo: TextView = itemView.findViewById(R.id.pi_item)
        val scoreTotal: TextView = itemView.findViewById(R.id.mid_act_score)

    }
}