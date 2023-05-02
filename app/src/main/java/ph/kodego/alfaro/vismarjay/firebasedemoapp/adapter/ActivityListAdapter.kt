package ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActivityListModel
import ph.kodego.alfaro.vismarjay.firebasedemoapp.performanceIndicator.MidActDetailsActivity

class ActivityListAdapter (private val activityList: ArrayList<ActivityListModel>, private val listener: MidActDetailsActivity):
    RecyclerView.Adapter<ActivityListAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ActivityListAdapter.ViewHolder{
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.perf_ind_item,parent,false)

        return ViewHolder(itemView,mListener)

    }

    override fun onBindViewHolder(holder: ActivityListAdapter.ViewHolder, position:Int){
        val activity = activityList[position]
        holder.activityNo.text = activity.activityTitle.toString()
        holder.scoreTotal.text = activity.scoreTotal.toString()
    }


    override fun getItemCount(): Int {
        return activityList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        val activityNo:TextView = itemView.findViewById(R.id.pi_item)
        val scoreTotal:TextView = itemView.findViewById(R.id.mid_act_score)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}