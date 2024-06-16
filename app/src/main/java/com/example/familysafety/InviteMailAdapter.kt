package com.example.familysafety

import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InviteMailAdapter(private val listInvites:List<String>,
    private  val onActionClick: OnActionClick
    ):RecyclerView.Adapter<InviteMailAdapter.ViewHolder>() {
    class ViewHolder(val itemView: View):RecyclerView.ViewHolder(itemView){
       val  name=itemView.findViewById<TextView>(R.id.emailInvite)
        val accept=itemView.findViewById<Button>(R.id.accept)
        val deny=itemView.findViewById<Button>(R.id.deny)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val item=inflater.inflate(R.layout.invitation,parent,false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return listInvites.size
    }

    override fun onBindViewHolder(holder: InviteMailAdapter.ViewHolder, position: Int) {
       val item=listInvites[position]
        holder.name.text=item

        holder.accept.setOnClickListener{
onActionClick.onAcceptClick(item)
        }

        holder.deny.setOnClickListener{

            onActionClick.onDenyClick(item)
        }
    }
    interface OnActionClick{

        fun onAcceptClick(mail:String){
        }
        fun onDenyClick(mail:String){

        }
    }



}