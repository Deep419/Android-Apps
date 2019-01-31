package com.example.tipcalculator;

import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText et_bill;
    RadioGroup rg;
    TextView tip_view, total, custombar_progress;
    int custombar_progress_int;
    double tip_percent = 0;
    double bill_input = 0.0, tip_amount,bill_total;
    SeekBar custom_bar;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.app_icon);

        et_bill = findViewById(R.id.et_bill); //bill amt input
        rg = findViewById(R.id.rg); //radio group
        custom_bar = findViewById(R.id.seekBar);
        custombar_progress = findViewById(R.id.tv_set_customtip);
        tip_view = findViewById(R.id.tv_set_tip);
        total = findViewById(R.id.tv_set_total);
        exit = findViewById(R.id.button);

        //set error for empty input
        if (et_bill.getText().toString().equalsIgnoreCase("")) {
            et_bill.setError("Enter Bill Total");
        }
        rg.check(R.id.rb_10); //Default selection is 10%
        custom_bar.setMax(50);
        custom_bar.setProgress(25);
        custombar_progress_int = custom_bar.getProgress();
        custombar_progress.setText(custombar_progress_int + "%");
        tip_view.setText("0.00");
        total.setText("0.00");

        et_bill.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN) {
                    try
                    {
                        String bill = et_bill.getText().toString();
                        bill_input = Double.parseDouble(bill);
                        Log.d("tt", "" + bill_input);
                    }
                    catch (Exception e)
                    {

                    }
                    tip_percent = 0.10;
                    tip_amount = bill_input*tip_percent;
                    tip_view.setText(String.valueOf(df2.format(tip_amount)));
                    bill_total = tip_amount + bill_input;
                    total.setText(String.valueOf(df2.format(bill_total)));

                    custom_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            custombar_progress_int = custom_bar.getProgress();
                            custombar_progress.setText(custombar_progress_int + "%");
                            Log.d("test","First.");
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });

                    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if(i==R.id.rb_10) {
                                tip_percent = 0.10;
                                tip_amount = bill_input*tip_percent;
                                tip_view.setText(String.valueOf(df2.format(tip_amount)));
                                bill_total = tip_amount + bill_input;
                                total.setText(String.valueOf(df2.format(bill_total)));
                            } else if(i==R.id.rb_15) {
                                tip_percent = 0.15;
                                tip_amount = bill_input*tip_percent;
                                tip_view.setText(String.valueOf(df2.format(tip_amount)));
                                bill_total = tip_amount + bill_input;
                                total.setText(String.valueOf(df2.format(bill_total)));
                            } else if(i==R.id.rb_18) {
                                tip_percent = 0.18;
                                tip_amount = bill_input*tip_percent;
                                tip_view.setText(String.valueOf(df2.format(tip_amount)));
                                bill_total = tip_amount + bill_input;
                                total.setText(String.valueOf(df2.format(bill_total)));
                            } else {
                                custombar_progress_int = custom_bar.getProgress();
                                custombar_progress.setText(custombar_progress_int + "%");
                                tip_percent = ((double)custom_bar.getProgress()/100);
                                tip_amount = bill_input*tip_percent;
                                tip_view.setText(String.valueOf(df2.format(tip_amount)));
                                bill_total = tip_amount + bill_input;
                                total.setText(String.valueOf(df2.format(bill_total)));
                                custom_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                        custombar_progress_int = custom_bar.getProgress();
                                        custombar_progress.setText(custombar_progress_int + "%");
                                        tip_percent = ((double)custom_bar.getProgress()/100);
                                        tip_amount = bill_input*tip_percent;
                                        bill_total = tip_amount + bill_input;
                                        if(rg.getCheckedRadioButtonId() == R.id.rb_other) {
                                            tip_view.setText(String.valueOf(df2.format(tip_amount)));
                                            total.setText(String.valueOf(df2.format(bill_total)));
                                            Log.d("test","Second.");
                                        }
                                        Log.d("test","Third.");
                                    }
                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }
                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                    }
                                });
                            }
                        }
                    });
                    return true;
                }
                return false;
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
