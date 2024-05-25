package com.example.focusfriend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class myAdapter(var dataset:ArrayList<noteEntity>):RecyclerView.Adapter<myAdapter.viewHolder>() {

    private lateinit var myListener:OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    fun setItemClickListener(Listener:OnItemClickListener){
        myListener=Listener
    }

    class viewHolder(val view:View,listener: OnItemClickListener):RecyclerView.ViewHolder(view) {
       val todo=view.findViewById<TextView>(R.id.todoText)
        val del=view.findViewById<ImageView>(R.id.imageButton)
        init {
            del.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.eachtodo, parent, false)
        return viewHolder(view,myListener)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.todo.text=dataset[position].text
    }

}