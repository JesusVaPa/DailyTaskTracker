package com.javap.dailytasktracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javap.dailytasktracker.ui.AddTaskActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.taskName.setText(task.getName());
        holder.taskDueDate.setText(task.getDueDate());
        holder.taskPriority.setText(task.getPriority());

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra("taskId", task.getId());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Delete Task")
                    .setMessage("¿Are you sure to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("tasks").child(task.getId());
                        taskRef.removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
                                    taskList.remove(position);
                                    notifyItemRemoved(position);
                                })
                                .addOnFailureListener(e -> Toast.makeText(context, "Error at deleting task", Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskName, taskDueDate, taskPriority;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskName = itemView.findViewById(R.id.textViewTaskName);
            taskDueDate = itemView.findViewById(R.id.textViewTaskDueDate);
            taskPriority = itemView.findViewById(R.id.textViewTaskPriority);
        }
    }
}
