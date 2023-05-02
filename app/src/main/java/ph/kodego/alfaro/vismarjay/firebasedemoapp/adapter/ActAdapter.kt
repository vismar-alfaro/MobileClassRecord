package ph.kodego.alfaro.vismarjay.firebasedemoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.ActModel

class ActAdapter(private val actList: ArrayList<ActModel>):
    RecyclerView.Adapter<ActAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ActAdapter.ViewHolder{
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.mid_act_item,parent,false)

        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ActAdapter.ViewHolder, position:Int){
        val act = actList[position]
        holder.lastName.text = act.lastName
        holder.firstName.text = act.firstName
    }


    override fun getItemCount(): Int {
        return actList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val lastName: TextView = itemView.findViewById(R.id.stud_lastname)
        val firstName: TextView = itemView.findViewById(R.id.stud_firstname)

    }

    fun getActList(): ArrayList<ActModel> {
        return actList
    }

}