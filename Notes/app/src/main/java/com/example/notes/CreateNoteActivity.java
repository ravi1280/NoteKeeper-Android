package com.example.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notes.model.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity {
    String idData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        idData = intent.getStringExtra("id");
        String titleData = intent.getStringExtra("title");
        String contentData = intent.getStringExtra("content");

        EditText title =findViewById(R.id.editTextText01);
        EditText contend =findViewById(R.id.editTextText02);

        if(title!=null){
            title.setText(titleData);

        }
        if (contend!=null){
            contend.setText(contentData);

        }

        Button btn02 = findViewById(R.id.button02);
        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(title.getText().toString().isEmpty()){
                    Toast.makeText(CreateNoteActivity.this,"Please Fill Tittle Field",Toast.LENGTH_LONG).show();

                } else if (contend.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this,"Please Fill Content Field",Toast.LENGTH_LONG).show();
                }else {
                    //save note
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(
                            CreateNoteActivity.this,
                            "mynotebook.db",
                            null,
                            1
                    );
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                           SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("title",title.getText().toString());
                            contentValues.put("content",contend.getText().toString());

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                            contentValues.put("date_created",simpleDateFormat.format(new Date()));

                            if(idData!=null){
                                int count = sqLiteDatabase.update("notes",
                                        contentValues,
                                        "`id` =?",
                                        new String[]{idData});

                                Log.i("AppNote", count + " Row Updated !");


                            }else {

                                long insertId = sqLiteDatabase.insert(
                                        "notes",
                                        null,
                                        contentValues);
                                Log.i("AppNote", String.valueOf(insertId));

                            }



                            sqLiteDatabase.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    title.setText("");
                                    contend.setText("");
                                    title.requestFocus();
                                    Toast.makeText(CreateNoteActivity.this,"Success",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }).start();
                }
            }
        });
    }
}