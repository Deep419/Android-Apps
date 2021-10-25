package com.example.to_dolistapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class view_tasks extends AppCompatActivity {

    private TextView title,priority,date,time;
    private String action="";
    private static LinkedList<Task> task_list = new LinkedList<>();
    private static int currentTaskCtr = 0;
    private static int id = 0;
    private TextView ctr2;
    private TextView ctr4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        Button btn_add,btn_gotofirst,btn_gotoprevious,btn_editcurrent,btn_deletecurrent,btn_gotonext,btn_gotolast;

        btn_add = findViewById(R.id.btn_plus);
        btn_gotofirst = findViewById(R.id.btn_bottom1_gotofirst);
        btn_gotoprevious = findViewById(R.id.btn_bottom2_gotoprevious);
        btn_editcurrent = findViewById(R.id.btn_bottom3_edit_current_task);
        btn_deletecurrent = findViewById(R.id.btn_bottom4_delete_current_task);
        btn_gotonext = findViewById(R.id.btn_bottom5_gotonext);
        btn_gotolast = findViewById(R.id.btn_bottom6_gotolast);

        title = findViewById(R.id.tv_tasktitle);
        priority = findViewById(R.id.tv_taskpriority);
        date = findViewById(R.id.tv_taskDate);
        time = findViewById(R.id.tv_taskTime);

        ctr2 = findViewById(R.id.tv_tasklist_2);
        ctr4 = findViewById(R.id.tv_tasklist_4);

        ctr2.setText(String.valueOf(currentTaskCtr));
        ctr4.setText(String.valueOf(task_list.size()));


        if(getIntent().getExtras()!=null){
            action = getIntent().getExtras().getString("ACTION");
        }
        if(action.equals("NewTask")){
            Task task = (Task) getIntent().getExtras().getSerializable("Task");
            task_list.add(task);
            currentTaskCtr = task_list.size();
            //Log.d("test", "ID : " + id);

            ctr4.setText(String.valueOf(task_list.size()));
            sort_list();
            currentTaskCtr = task.getId();
            update_taskList(task);
            Log.d("test","Size: " + String.valueOf(task_list.size()));
        }
        if(action.equals("DoneEdit")) {
            Task task = (Task) getIntent().getExtras().getSerializable("Task");
            task_list.get(task.id-1).setDay(task.getDay());
            task_list.get(task.id-1).setHour(task.getHour());
            task_list.get(task.id-1).setMin(task.getMin());
            task_list.get(task.id-1).setMonth(task.getMonth());
            task_list.get(task.id-1).setYear(task.getYear());
            task_list.get(task.id-1).setPriority(task.getPriority());
            task_list.get(task.id-1).setTitle(task.getTitle());
            sort_list();
            currentTaskCtr = task.getId();
            update_taskList(task);
        }

        Log.d("test","SIZE PRIOR : " + task_list.size());



        Log.d("test","In MAIN ......");

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // To implement, linked list of
                id +=1;
                Intent explicitIntent = new Intent(view_tasks.this, create_tasks.class);
                explicitIntent.putExtra("ACTION","AddID");
                explicitIntent.putExtra("TASKID",id);
                startActivity(explicitIntent);

            }
        });


        btn_gotofirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task_list.size() == 0) {
                    Toast.makeText(view_tasks.this,"No Added Task. Click + to add.", Toast.LENGTH_SHORT).show();
                }
                else {
                    currentTaskCtr = 1;
                    Task task = task_list.getFirst();
                    update_taskList(task);
                }
            }
        });
        btn_gotoprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task_list.size() == 0) {
                    Toast.makeText(view_tasks.this,"No Added Task. Click + to add.", Toast.LENGTH_SHORT).show();
                }
                else if(currentTaskCtr == 1) {
                    Toast.makeText(view_tasks.this,"Current Task is the first task.", Toast.LENGTH_SHORT).show();
                }
                else {
                    currentTaskCtr -= 1;
                    Task task = task_list.get(currentTaskCtr-1);
                    update_taskList(task);
                }
            }
        });
        btn_editcurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task_list.size() == 0) {
                    Toast.makeText(view_tasks.this,"No Added Task. Click + to add.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("test","CurrentTaskCtr : " + currentTaskCtr + " Size : " + task_list.size() + " ID : " + id);
                    Task task = task_list.get(currentTaskCtr - 1);
                    Intent explicitIntent = new Intent(view_tasks.this, create_tasks.class);
                    explicitIntent.putExtra("ACTION","EditCurrent");
                    explicitIntent.putExtra("TASK",task);
                    startActivity(explicitIntent);
                    update_taskList(task);
                }
            }
        });
        btn_deletecurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task_list.size() == 0) {
                    Toast.makeText(view_tasks.this,"No Added Task. Click + to add.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(currentTaskCtr < task_list.size()) {
                        for(int i=currentTaskCtr;i<task_list.size();i++) {
                            task_list.get(i).setId(i);
                        }
                    }
                    task_list.remove(currentTaskCtr - 1);
                    try {
                        currentTaskCtr = 1;
                        update_taskList(task_list.getFirst());
                    }
                    catch(Exception E) {
                        ctr2.setText(String.valueOf(0));
                        ctr4.setText(String.valueOf(0));
                        title.setText("");
                        priority.setText("");
                        time.setText("");
                        date.setText("");
                    }
                }
            }
        });
        btn_gotonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task_list.size() == 0) {
                    Toast.makeText(view_tasks.this,"No Added Task. Click + to add.", Toast.LENGTH_SHORT).show();
                }
                else if(currentTaskCtr == task_list.size()) {
                    Toast.makeText(view_tasks.this,"Current Task is the last task.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Task task = task_list.get(currentTaskCtr);
                    currentTaskCtr += 1;
                    update_taskList(task);
                }
            }
        });
        btn_gotolast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task_list.size() == 0) {
                    Toast.makeText(view_tasks.this,"No Added Task. Click + to add.", Toast.LENGTH_SHORT).show();
                }
                else {
                    currentTaskCtr = task_list.size();
                    Task task = task_list.getLast();
                    update_taskList(task);
                }
            }
        });
    }

    private void update_taskList(Task current_task) {
        ctr2 = findViewById(R.id.tv_tasklist_2);
        ctr2.setText(String.valueOf(current_task.getId()));
        ctr4 = findViewById(R.id.tv_tasklist_4);
        ctr4.setText(String.valueOf(task_list.size()));
        title.setText(current_task.getTitle());
        priority.setText(current_task.getPriority());
        Log.d("test","Get Priority : " + current_task.getPriority());
        String time_string;
        //Calendar task_time = current_task.getCalendar();
        int hourOfDay = current_task.getHour();
        int minute = current_task.getMin();
        if (hourOfDay > 12) {
            time_string = String.format("%d : %02d PM", (hourOfDay - 12), minute);
            time.setText(time_string);
        } else {
            if (hourOfDay == 0) {
                time_string = String.format("12 : %02d AM", minute);
            } else {
                time_string = String.format("%d : %02d AM", hourOfDay, minute);
            }
            time.setText(time_string);
        }
        Log.d("test","Date String : " + time_string);
        time.setText(time_string);
        date.setText(current_task.getMonth() + "/" + current_task.getDay() + "/" + current_task.getYear());
        Log.d("test","Size: " + String.valueOf(task_list.size()));
    }

    private void sort_list() {
        if(task_list.size() > 1) {
            Collections.sort(task_list, new Comparator<Task>() {
                @Override
                public int compare(Task d1, Task d2) {

                    Calendar cal1 = Calendar.getInstance();
                    cal1.set(d1.getYear(), d1.getMonth(), d1.getDay(), d1.getHour(), d1.getMin());
                    Log.d("cal"," " + cal1.get(Calendar.YEAR) + " " + cal1.get(Calendar.MONTH) +" " + cal1.get(Calendar.DAY_OF_MONTH) + " " + cal1.get(Calendar.HOUR_OF_DAY) + " " + cal1.get(Calendar.MINUTE));

                    Calendar cal2 = Calendar.getInstance();
                    cal2.set(d2.getYear(), d2.getMonth(), d2.getDay(), d2.getHour(), d2.getMin());
                    Log.d("cal"," " + cal2.get(Calendar.YEAR) + " " + cal2.get(Calendar.MONTH) +" " + cal2.get(Calendar.DAY_OF_MONTH) + " " + cal2.get(Calendar.HOUR_OF_DAY) + " " + cal2.get(Calendar.MINUTE));


                    Log.d("test_result","...IN COMPARE.. : D1 " + + (int)cal1.getTimeInMillis() + "| D2 " + (int)cal2.getTimeInMillis());
                    return ((int)cal1.getTimeInMillis() - (int)cal2.getTimeInMillis());
                }
            });
            for(int i = 0;i<task_list.size();i++) {
                task_list.get(i).setId(i+1);
            }
        }
    }
}
