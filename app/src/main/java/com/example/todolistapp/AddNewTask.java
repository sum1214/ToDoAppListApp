package com.example.todolistapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todolistapp.model.ToDoModel;
import com.example.todolistapp.utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "Action Bottom Dialog";
    private EditText newTaskEdt;
    private Button saveBtn;
    private DatabaseHandler db;
    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskEdt = view.findViewById(R.id.new_task_et);
        newTaskEdt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(newTaskEdt, InputMethodManager.SHOW_IMPLICIT);
        saveBtn = view.findViewById(R.id.new_task_btn);
        db = new DatabaseHandler(getActivity());
        db.openDatabase();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle!=null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskEdt.setText(task);
            if (task.length()>0) {
                saveBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.holo_green_dark));
            }
        }
        newTaskEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s==null || s.toString().isEmpty()) {
                   saveBtn.setEnabled(false);
                   saveBtn.setTextColor(Color.GRAY);
               } else {
                   saveBtn.setEnabled(true);
                   saveBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.holo_green_dark));
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskEdt.getText().toString();
                if (finalIsUpdate) {
                    db.updateTask(bundle.getInt("id"),text);
                } else {
                    ToDoModel toDoModel = new ToDoModel();
                    toDoModel.setTask(text);
                    toDoModel.setStatus(0);
                    db.insertTask(toDoModel);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}
