package com.example.familysafety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MemberAdapter(private val listMembers: List<MemberModel>) : RecyclerView.Adapter<MemberAdapter.viewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.viewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val item=inflater.inflate(R.layout.homeview,parent,false)
        return viewHolder(item)

    }

    override fun onBindViewHolder(holder: MemberAdapter.viewHolder, position: Int) {
        val item=listMembers[position]
        holder.name.text=item.name

    }

    override fun getItemCount(): Int {
        return listMembers.size
    }


    class viewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

val imageUser=item.findViewById<ImageView>(R.id.mainImage)
        val name=item.findViewById<TextView>(R.id.nameText)


    }
}