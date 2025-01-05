package com.example.notes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.model.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
       Button btn1 = findViewById(R.id.mainbtn01);
       btn1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(MainActivity.this,CreateNoteActivity.class);
               startActivity(i);
           }
       });

      RecyclerView recyclerView =  findViewById(R.id.recyclerView01);
        SQLiteHelper sqLiteHelper = new SQLiteHelper(
                MainActivity.this,
                "mynotebook.db",
                null,
                1
        );
        new Thread(new Runnable() {
            @Override
            public void run() {
               SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
              Cursor cursor = sqLiteDatabase.query(
                      "notes",
                      null,
                      null,
                      null,
                      null,
                      null,
                      "`id` DESE"
              );

                NoteAdapter noteAdapter =  new NoteAdapter(cursor);
                recyclerView.setAdapter(noteAdapter);

              while (cursor.moveToNext()){
                 String title = cursor.getString(1);
                  Log.i("AppNote", String.valueOf(title));
              }


            }
        }).start();


    }
}

class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    Cursor cursor;

    public NoteAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView TitleView;
        TextView ContendView;
        TextView DateView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleView =itemView.findViewById(R.id.NtextView01);
            ContendView =itemView.findViewById(R.id.NtextView02);
            DateView =itemView.findViewById(R.id.NtextView03);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.note_item, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        cursor.move(position);
        holder.TitleView.setText(cursor.getString(1));
        holder.ContendView.setText(cursor.getString(2));
        holder.DateView.setText(cursor.getString(3));

    }

    @Override
    public int getItemCount() {

        return cursor.getCount();
    }


}