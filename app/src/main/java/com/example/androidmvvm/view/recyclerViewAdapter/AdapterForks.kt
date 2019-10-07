package com.example.androidmvvm.view.recyclerViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Fork

class AdapterForks(
    val context:Context,
    var mList:ArrayList<Fork> = arrayListOf()
): RecyclerView.Adapter<AdapterForks.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_fork, null)
        )
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view){

    }
}