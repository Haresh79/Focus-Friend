package com.example.focusfriend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class note : Fragment() {
    private lateinit var database:databaseNote
    lateinit var noteArray:ArrayList<noteEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val applicationContext= activity?.applicationContext
        val view=inflater.inflate(R.layout.fragment_note, container, false)
        val addBtn= view.findViewById<Button>(R.id.addBtn)
        val editText= view.findViewById<EditText>(R.id.editTextText)
        val recyclerView=view.findViewById<RecyclerView>(R.id.todorv)
        recyclerView.layoutManager= LinearLayoutManager(view.context)
        noteArray= arrayListOf()

            database=Room.databaseBuilder(applicationContext!!, databaseNote::class.java, "Todo").build()

        addBtn.setOnClickListener {
            val input=editText.text
            if (input.isNotEmpty()){
                GlobalScope.launch {
                    database.noteDao().insert(noteEntity(0, input.toString()))
                    input.clear()
                }
            }
        }
        database.noteDao().showTodo().observe(viewLifecycleOwner,{
            noteArray=it as ArrayList<noteEntity>
            var adapter = myAdapter(noteArray)
            recyclerView.adapter = adapter
            adapter.setItemClickListener(object :myAdapter.OnItemClickListener{
                override fun onItemClicked(position: Int) {
                    deleteTodo(position)
                    adapter=myAdapter(noteArray)
                    recyclerView.adapter=adapter
                }

            })
        })

        return view
    }

    fun deleteTodo(position:Int)= lifecycleScope.launch(Dispatchers.IO){
        database.noteDao().delete( noteArray[position])
    }

}