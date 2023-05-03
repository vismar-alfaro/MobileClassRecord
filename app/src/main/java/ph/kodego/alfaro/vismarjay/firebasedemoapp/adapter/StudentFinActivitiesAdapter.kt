package ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.activities.StudentFinActivitiesActivity
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel

class StudentFinActivitiesAdapter(private val activityList: ArrayList<ActivityListModel>, private val listener: StudentFinActivitiesActivity):
    RecyclerView.Adapter<StudentFinActivitiesAdapter.ViewHolder>() {


    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentFinActivitiesAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.perf_ind_item, parent, false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: StudentFinActivitiesAdapter.ViewHolder, position: Int) {
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