package com.example.bmicalculatorapp;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText years, pounds, feet, inches;
    private float mass,height;
    private float bmi =0,weight_lower,weight_higher;
    private TextView list1, list2, list3, list4, list1_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        years = findViewById(R.id.editTextYears);
        pounds = findViewById(R.id.editTextPounds);
        feet = findViewById(R.id.editTextFeet);
        inches = findViewById(R.id.editTextInches);
        Button mButton = findViewById(R.id.button);

        list1 = findViewById(R.id.list1);
        list1_5 = findViewById(R.id.list1_5);
        list2 = findViewById(R.id.list2);
        list3 = findViewById(R.id.list3);
        list4 = findViewById(R.id.list4);

        list1.setVisibility(View.INVISIBLE);
        list2.setVisibility(View.INVISIBLE);
        list3.setVisibility(View.INVISIBLE);
        list4.setVisibility(View.INVISIBLE);
        list1_5.setVisibility(View.INVISIBLE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (years.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Enter Years", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pounds.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Enter Pounds", Toast.LENGTH_LONG).show();
                    return;
                }
                if (feet.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Enter Feet", Toast.LENGTH_LONG).show();
                    return;
                }
                if (inches.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Enter Inches", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Integer.parseInt(years.getText().toString()) < 18 ) {
                    Toast.makeText(MainActivity.this, "Age should be 18 or more!", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("demo", "onClick: ");
                height = (12 * Float.parseFloat(feet.getText().toString()))
                        + Float.parseFloat(inches.getText().toString());
                mass = Integer.parseInt(pounds.getText().toString());
                bmi = (float) 703 * (mass/(height * height));
                weight_higher = (height * height * 25) / 703;
                weight_lower = (float) (height * height * 18.5) / 703;
                Log.d("demo", "BMI : " + bmi + " Height : " + height);
                list1.setVisibility(View.VISIBLE);
                list2.setVisibility(View.VISIBLE);
                list3.setVisibility(View.VISIBLE);
                list4.setVisibility(View.VISIBLE);
                list1_5.setVisibility(View.VISIBLE);

                list1.setText(getString(R.string.list1, bmi));

                if (bmi < 18.5) {
                    list1_5.setTextColor(Color.RED);
                    list1_5.setText("(Underweight)");
                    list3.setText(getString(R.string.list3_needToGain,weight_lower-mass));
                    list4.setText(getString(R.string.list4,18.5));
                } else if (bmi < 25) {
                    list1_5.setTextColor(Color.GREEN);
                    list1_5.setText("(Normal)");
                    list3.setText(getString(R.string.list3_good));

                } else if (bmi < 30) {
                    list1_5.setTextColor(Color.YELLOW);
                    list1_5.setText("(Overweight)");
                    list3.setText(getString(R.string.list3_needToLose,mass-weight_higher));
                    list4.setText(getString(R.string.list4,(float) 25));

                } else {
                    list1_5.setTextColor(Color.RED);
                    list1_5.setText("(Obese)");
                    list3.setText(getString(R.string.list3_needToLose,mass-weight_higher));
                    list4.setText(getString(R.string.list4,(float) 25));

                }
            }
        });
    }
}
