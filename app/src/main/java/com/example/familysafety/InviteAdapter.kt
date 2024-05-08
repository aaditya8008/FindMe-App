package com.example.familysafety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InviteAdapter (private val list:List<ContactModel>):RecyclerView.Adapter<InviteAdapter.ViewHolder>(){

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
     val image=itemView.findViewById<ImageView>(R.id.inviteProfile)
     val name= itemView.findViewById<TextView>(R.id.inviteName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val item=inflater.inflate(R.layout.invite,parent,false)
        return ViewHolder(item)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list[position]
        holder.name.text=item.name.toString()

    }
}