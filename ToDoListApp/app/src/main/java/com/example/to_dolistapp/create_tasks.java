package com.example.to_dolistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class create_tasks extends AppCompatActivity {

    private Button btn_save;
    private RadioGroup rg;
    private String title ="",priority="";
    private EditText time_text,date_text,title_text;
    private RadioButton rb;
    private String action = "";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tasks);

        title_text = findViewById(R.id.et_title);

        date_text = findViewById(R.id.et_date);
        date_text.setOnKeyListener(null);
        date_text.setFocusable(false);
        time_text = findViewById(R.id.et_time);
        time_text.setOnKeyListener(null);
        time_text.setFocusable(false);
        rg = findViewById(R.id.radioGroup);

        action = getIntent().getExtras().getString("ACTION");

        if(action.equals("EditCurrent")) {
            Task task = (Task) getIntent().getExtras().getSerializable("TASK");
            id = task.getId();
            title_text.setText(task.getTitle());
            String time_string = "";
            int hourOfDay = task.getHour();
            int minute = task.getMin();
            if (hourOfDay > 12) {
                time_string = String.format("%d : %02d PM", (hourOfDay - 12), minute);
                time_text.setText(time_string);
            } else {
                if (hourOfDay == 0) {
                    time_string = String.format("12 : %02d AM", minute);
                } else {
                    time_string = String.format("%d : %02d AM", hourOfDay, minute);
                }
                time_text.setText(time_string);
            }

            Log.d("test", "Date String : " + time_string);
            time_text.setText(time_string);
            date_text.setText(task.getMonth() + "/" + task.getDay() + "/" + task.getYear());
            if (task.getPriority().equals("High")) {
                rg.check(R.id.rb_2_high);
            } else if (task.getPriority().equals("Medium")) {
                rg.check(R.id.rb_2_med);
            } else {
                rg.check(R.id.rb_2_low);
            }
        }
        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear,mMonth,mDay;
                if(date_text.getText().toString().equals("")) {
                    mYear = Calendar.getInstance().get(Calendar.YEAR);
                    mMonth = Calendar.getInstance().get(Calendar.MONTH);
                    mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    Log.d("Time", "Date [91] : " + mYear +" - " + mMonth + " - " +mDay);
                }
                else {
                    String[] items= date_text.getText().toString().split("/");
                    Log.d("Time","Time Size [94]: "+ items.length + "...."+date_text.getText().toString());
                    mYear = Integer.parseInt(items[2]);
                    mMonth = Integer.parseInt(items[0])-1;
                    mDay = Integer.parseInt(items[1]);
                }
                Log.d("Time", "Date [98] : " + mYear +" - " + mMonth + " - " +mDay);
                DatePickerDialog dialog = new DatePickerDialog(create_tasks.this, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //time_date.set(year, month, dayOfMonth);
                                date_text.setText("" + (month+1) + "/" + dayOfMonth + "/" + year);
                                Log.d("month", " Month  [103] : " + month);
                            }
                        }, mYear, mMonth, mDay);
                dialog.setTitle("Select Date");
                dialog.show();
            }
        });

        time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour,minute;
                Log.d("Time"," Time [116] " + time_text.getText().toString());
                if(time_text.getText().toString().equals("")) {
                    hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    minute = Calendar.getInstance().get(Calendar.MINUTE);
                }
                else {
                    String[] items= time_text.getText().toString().split(" ");
                    minute = Integer.parseInt(items[2]);
                    hour = Integer.parseInt(items[0]);
                    if(items[3].equals("PM")) {
                        hour += 12;
                    }
                    Log.d("Time","Time Size [129]: "+ hour + " Minute :"+minute);
                }
                TimePickerDialog dialog = new TimePickerDialog(create_tasks.this, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String text = "";
                                if (hourOfDay > 12) {
                                    text = String.format("%d : %02d PM", (hourOfDay - 12), minute);
                                    time_text.setText(text);
                                } else {
                                    if (hourOfDay == 0) {
                                        text = String.format("12 : %02d AM", minute);
                                    } else {
                                        text = String.format("%d : %02d AM", hourOfDay, minute);
                                    }
                                    time_text.setText(text);
                                }
                            }
                        }, hour, minute, false);
                dialog.setTitle("Select Time");
                dialog.show();
            }
        });

        btn_save = findViewById(R.id.btn_2_Save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rb = findViewById(rg.getCheckedRadioButtonId());
                if (rb!=null) {
                    priority = rb.getText().toString();
                }
                title = title_text.getText().toString();

                if(title.equals("")){
                    Toast.makeText(create_tasks.this,"Enter Task Title", Toast.LENGTH_SHORT).show();
                }
                else if(priority.equals("")){
                    Toast.makeText(create_tasks.this,"Enter Priority", Toast.LENGTH_SHORT).show();
                }
                else if (time_text.getText().toString().equals("")) {
                    Toast.makeText(create_tasks.this,"Select Time", Toast.LENGTH_SHORT).show();
                }
                else if (date_text.getText().toString().equals("")) {
                    Toast.makeText(create_tasks.this,"Select Date", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(action.equals("AddID")) {
                        id = getIntent().getExtras().getInt("TASKID");
                    }
                    String[] date_items= date_text.getText().toString().split("/");
                    int mYear = Integer.parseInt(date_items[2]);
                    int mMonth = Integer.parseInt(date_items[0]);
                    int mDay = Integer.parseInt(date_items[1]);

                    String[] time_items= time_text.getText().toString().split(" ");
                    int minute = Integer.parseInt(time_items[2]);
                    int hour = Integer.parseInt(time_items[0]);
                    if(time_items[3].equals("PM")) {
                        hour += 12;
                    }

                    Log.d("Final","Title [166] : "+title);
                    Log.d("Final","Priority [167] : "+priority);
                    Log.d("Final", "Date ---[193] : " + mYear +" - " + mMonth + " - " +mDay + " {"+date_text.getText().toString()+"}");
                    Log.d("Final","Time ---[196]: "+ hour + " Minute :"+minute + " {"+time_text.getText().toString()+"}");

                    Task task = new Task(title,priority,id,mYear,mMonth,mDay,hour,minute);
                    Intent explicitIntent = new Intent(create_tasks.this, view_tasks.class);
                    explicitIntent.putExtra("Task", task);
                    if(action.equals("EditCurrent")) {
                        explicitIntent.putExtra("ACTION", "DoneEdit");
                    }
                    else {
                        explicitIntent.putExtra("ACTION", "NewTask");
                    }
                    startActivity(explicitIntent);
                }
            }
        });
    }
}