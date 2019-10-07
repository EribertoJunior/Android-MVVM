package com.example.androidmvvm.view

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.androidmvvm.R
import com.example.androidmvvm.view_model.ForkViewModel

import kotlinx.android.synthetic.main.activity_forks.*

class ForksActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ForkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forks)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

}
