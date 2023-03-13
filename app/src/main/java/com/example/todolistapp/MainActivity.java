package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolistapp.adapter.ToDoAdapter;
import com.example.todolistapp.model.ToDoModel;
import com.example.todolistapp.utils.DatabaseHandler;
import com.example.todolistapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private ActivityMainBinding binding;
    private ToDoAdapter toDoAdapter;
    private List<ToDoModel> toDoData;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        db = new DatabaseHandler(this);
        db.openDatabase();
        toDoData = new ArrayList<>();
        binding.tasksRv.setLayoutManager(new LinearLayoutManager(this));
        toDoAdapter = new ToDoAdapter(toDoData,this,db);
        binding.tasksRv.setAdapter(toDoAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(binding.tasksRv);
        toDoData = db.getAllTasks();
        Collections.reverse(toDoData);
        toDoAdapter.setTasks(toDoData);
        toDoAdapter.notifyDataSetChanged();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        toDoData = db.getAllTasks();
        Collections.reverse(toDoData);
        toDoAdapter.setTasks(toDoData);
        toDoAdapter.notifyDataSetChanged();
    }
}