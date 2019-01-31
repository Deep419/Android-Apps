package com.example.inclass04;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements GeneratePasswordAsync.IData{
    private SeekBar seekbar_Count, seekbar_Length;
    private TextView st_count,st_length;
    private Button btn_thread, btn_async;
    private String[] master_strings;
    private ExecutorService threadPool;
    private Handler handler;
    private ArrayList<String> password = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekbar_Count = findViewById(R.id.seekBar_count);
        seekbar_Count.setMax(10-1);
        seekbar_Count.setProgress(0);

        seekbar_Length = findViewById(R.id.seekBar_length);
        seekbar_Length.setMax(23-8);
        seekbar_Length.setProgress(0);

        st_count = findViewById(R.id.set_1_selectPassCount);
        st_count.setText(String.valueOf(1));

        st_length = findViewById(R.id.set_1_selectPassLength);
        st_length.setText(String.valueOf(8));


        seekbar_Count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                st_count.setText(String.valueOf(i+1));
                //Log.d("test","First.");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        seekbar_Length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                st_length.setText(String.valueOf(i+8));
                //Log.d("test","First.");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Thread Pool
        threadPool = Executors.newFixedThreadPool(2);
        progressDialog = new ProgressDialog(MainActivity.this);

        //Buttons
        btn_thread = findViewById(R.id.btn_1_thread);
        btn_async = findViewById(R.id.btn_1_async);

        btn_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMax(seekbar_Count.getProgress()+1);
                progressDialog.setProgress(0);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                Log.d("test","Here2");
                Log.d("test","getProgress" +seekbar_Count.getProgress()+1);
                for(int i=1;i<=seekbar_Count.getProgress()+1;i++) {
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.incrementProgressBy(1);
                            Log.d("test","Here3");
                            String result = Util.getPassword(Integer.parseInt(st_length.getText().toString()));
                            Message msg = new Message();
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    });
                }

            }
        });

        btn_async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GeneratePasswordAsync(MainActivity.this).execute(String.valueOf(seekbar_Count.getProgress()+1)+" "+st_length.getText().toString());
            }
        });

        handler = new Handler(new Handler.Callback() {
             @Override
             public boolean handleMessage(Message message) {
                 String pass = (String)message.obj;
                 Log.d("test","Here1");

                 password.add(pass);
                 if(password.size()==seekbar_Count.getProgress()+1) {
                     Log.d("test","Here");
                     progressDialog.dismiss();

                     //password.toArray(String[] strings);
                     String[] strings = password.toArray(new String[password.size()]);
                     AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                     builder.setItems(strings, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
//                             ListView lw = ((AlertDialog)dialogInterface).getListView();
//                             Object checkedItem = lw.getAdapter().getItem(lw.getSelectedItemPosition());
//                             Log.d("Selected : ", String.valueOf(i));
                         }
                     });
                    builder.create().show();

                     return true;
                 }
                 return false;
             }
         });
    }

    @Override
    public void onPre() {
        dialog = new ProgressDialog(MainActivity.this);
        Log.d("test", "3. Pre Main : " + Thread.currentThread().getId());
        dialog.setMessage("Generating Passwords...");
        dialog.show();

    }

    @Override
    public void handleData(String[] strings) {
        master_strings = strings;
    }

    @Override
    public void onPost() {
        if (dialog.isShowing()) {
            dialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setItems(master_strings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                             ListView lw = ((AlertDialog)dialogInterface).getListView();
//                             Object checkedItem = lw.getAdapter().getItem(lw.getSelectedItemPosition());
//                             Log.d("Selected : ", String.valueOf(i));
                }
            });
            builder.create().show();
        }
    }
}
