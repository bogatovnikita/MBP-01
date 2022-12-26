package com.example.test

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler)

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            }
            delay(2000)
            withContext(Dispatchers.Main){
                recycler.layoutManager = GridLayoutManager(this@MainActivity, 2)
            }
        }
        val adapter = Adapter()
        recycler.adapter = adapter
        adapter.submitList(listOf("231231", "", "", "","","","","","","","","","","","","",))
        Log.d("!!!", "onCreate")




    }

    private class Adapter : ListAdapter<String, ViewHolder>(
        object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    ){
        @SuppressLint("NewApi")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.d("!!!", "onCreateViewHolder")
            return object : ViewHolder(TextView(parent.context).also {
                it.text = "test"
                it.setTextColor(parent.context.getColor(R.color.purple_200))
            }){

            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.d("!!!", "onBindViewHolder")
            (holder.itemView as TextView).apply {
                text = "some text"
            }
        }
    }
}