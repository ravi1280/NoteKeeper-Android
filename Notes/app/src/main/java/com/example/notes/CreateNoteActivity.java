package com.example.notes;

import android.os.Bundle;
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

public class CreateNoteActivity extends AppCompatActivity {

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
        Button btn02 = findViewById(R.id.button02);
        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText title =findViewById(R.id.editTextText01);
                EditText contend =findViewById(R.id.editTextText02);

                if(title.getText().toString().isEmpty()){
                    Toast.makeText(CreateNoteActivity.this,"Please Fill Tittle Field",Toast.LENGTH_LONG).show();

                } else if (contend.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this,"Please Fill Content Field",Toast.LENGTH_LONG).show();
                }else {
                    //save note

                }
            }
        });

    }
}