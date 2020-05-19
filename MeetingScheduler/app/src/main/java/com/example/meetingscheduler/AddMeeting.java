package com.example.meetingscheduler;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddMeeting extends AppCompatActivity {

    private Button btn_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear,mMonth,mDay;
                if(btn_date.getText().toString().equals("")) {
                    mYear = Calendar.getInstance().get(Calendar.YEAR);
                    mMonth = Calendar.getInstance().get(Calendar.MONTH);
                    mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    Log.d("Time", "Date [91] : " + mYear +" - " + mMonth + " - " +mDay);
                }
                else {
                    String[] items= btn_date.getText().toString().split("/");
                    Log.d("Time","Time Size [94]: "+ items.length + "...."+btn_date.getText().toString());
                    mYear = Integer.parseInt(items[2]);
                    mMonth = Integer.parseInt(items[0])-1;
                    mDay = Integer.parseInt(items[1]);
                }
                Log.d("Time", "Date [98] : " + mYear +" - " + mMonth + " - " +mDay);
                DatePickerDialog dialog = new DatePickerDialog(AddMeeting.this, android.R.style.Theme_DeviceDefault_Dialog_Alert,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //time_date.set(year, month, dayOfMonth);
                                btn_date.setText("" + (month+1) + "/" + dayOfMonth + "/" + year);
                                Log.d("month", " Month  [103] : " + month);
                            }
                        }, mYear, mMonth, mDay);
                dialog.setTitle("Select Date");
                dialog.show();
            }
        });

    }
}


