package com.example.todolistapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.AddNewTask;
import com.example.todolistapp.model.ToDoModel;
import com.example.todolistapp.R;
import com.example.todolistapp.utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    List<ToDoModel> toDoData;
    Activity activity;
    private DatabaseHandler db;

    public ToDoAdapter(List<ToDoModel> toDoData, Activity activity,DatabaseHandler db) {
        this.toDoData = toDoData;
        this.activity = activity;
        this.db = db;
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.task_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {
        db.openDatabase();
       ToDoModel toDoModel = toDoData.get(position);
       holder.task_cb.setText(toDoModel.getTask());
       holder.task_cb.setChecked(toBoolean(toDoModel.getStatus()));
       holder.task_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                   db.updateStatus(toDoModel.getId(),1);
               } else {
                   db.updateStatus(toDoModel.getId(),0);
               }
           }
       });
    }
    private boolean toBoolean(int num) {
        return num!=0;
    }

    @Override
    public int getItemCount() {
        return toDoData.size();
    }
    public void editItem(int pos) {
        ToDoModel item = toDoData.get(pos);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(((AppCompatActivity)activity).getSupportFragmentManager(),AddNewTask.TAG);

    }
    public void deleteItem(int pos) {
        ToDoModel item = toDoData.get(pos);
        db.deleteTask(item.getId());
        toDoData.remove(pos);
        notifyItemRemoved(pos);
    }
    public Context getContext() {
        return activity;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task_cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task_cb = itemView.findViewById(R.id.todo_cb);
        }
    }
    public void setTasks(List<ToDoModel> toDoData) {
        this.toDoData = toDoData;
    }
}
