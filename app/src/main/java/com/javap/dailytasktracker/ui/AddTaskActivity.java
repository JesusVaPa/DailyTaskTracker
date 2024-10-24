package com.javap.dailytasktracker.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.javap.dailytasktracker.R;
import com.javap.dailytasktracker.Task;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextName, editTextDueDate, editTextPriority;
    private Button buttonSaveTask;
    private DatabaseReference databaseTasks;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        editTextName = findViewById(R.id.editTextTaskName);
        editTextDueDate = findViewById(R.id.editTextTaskDueDate);
        editTextPriority = findViewById(R.id.editTextTaskPriority);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);

        databaseTasks = FirebaseDatabase.getInstance().getReference("tasks");
        taskId = getIntent().getStringExtra("taskId");

        if (taskId != null) {
            loadTaskData(taskId);
        }

        editTextDueDate.setOnClickListener(view -> showDatePickerDialog());

        buttonSaveTask.setOnClickListener(view -> {
            if (taskId == null) {
                saveNewTask();
            } else {
                updateTask(taskId);
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editTextDueDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void loadTaskData(String taskId) {
        DatabaseReference taskRef = databaseTasks.child(taskId);
        taskRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Task task = snapshot.getValue(Task.class);
                    if (task != null) {
                        editTextName.setText(task.getName());
                        editTextDueDate.setText(task.getDueDate());
                        editTextPriority.setText(task.getPriority());
                    }
                } else {
                    Toast.makeText(AddTaskActivity.this, "Task not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddTaskActivity.this, "Error loading task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        String name = editTextName.getText().toString();
        String dueDate = editTextDueDate.getText().toString();
        String priority = editTextPriority.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dueDate) || TextUtils.isEmpty(priority)) {
            Toast.makeText(this, "Please, fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveNewTask() {
        if (!validateInputs()) {
            return;
        }

        String name = editTextName.getText().toString();
        String dueDate = editTextDueDate.getText().toString();
        String priority = editTextPriority.getText().toString();

        String id = databaseTasks.push().getKey();
        assert id != null;

        Task task = new Task(id, name, dueDate, priority, false);
        databaseTasks.child(id).setValue(task)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddTaskActivity.this, "Task saved", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddTaskActivity.this, "Error saving task", Toast.LENGTH_SHORT).show());
    }

    private void updateTask(String taskId) {
        if (!validateInputs()) {
            return;
        }

        String name = editTextName.getText().toString();
        String dueDate = editTextDueDate.getText().toString();
        String priority = editTextPriority.getText().toString();

        Task updatedTask = new Task(taskId, name, dueDate, priority, false);
        databaseTasks.child(taskId).setValue(updatedTask)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddTaskActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddTaskActivity.this, "Error updating task", Toast.LENGTH_SHORT).show());
    }
}
