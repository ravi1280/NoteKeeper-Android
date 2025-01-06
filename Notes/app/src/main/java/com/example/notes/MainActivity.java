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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshnote();
    }
    private void refreshnote(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView01);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
            }
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.i("AppNote", "On Swiped");

                NoteAdapter.NoteViewHolder holder = (NoteAdapter.NoteViewHolder) viewHolder;

                SQLiteHelper sqLiteHelper = new SQLiteHelper(
                        viewHolder.itemView.getContext(),
                        "mynotebook.db",
                        null,
                        1
                );

                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                        int row = sqLiteDatabase.delete(
                                "notes",
                                "id=?",
                                new String[]{holder.id}
                        );
                        Log.i("AppNote", row + "Rows Deleted");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshnote();
                            }
                        });
                    }
                }).start();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

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
                        "`id` DESC"
                );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NoteAdapter noteAdapter =  new NoteAdapter(cursor);
                        recyclerView.setAdapter(noteAdapter);
                    }
                });
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

        View containerView;
        String id ;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleView =itemView.findViewById(R.id.NtextView01);
            ContendView =itemView.findViewById(R.id.NtextView02);
            DateView =itemView.findViewById(R.id.NtextView03);
            containerView =itemView;
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.note_item, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
//        Log.i("AppNote",String.valueOf(position));

        cursor.moveToPosition(position);

        holder.id = cursor.getString(0);
        String title = cursor.getString(1);
        String content = cursor.getString(2);
        String date = cursor.getString(3);

        holder.TitleView.setText(title);
        holder.ContendView.setText(content);
        holder.DateView.setText(date);

        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AppNote","Clicked");
                Intent intent = new Intent(view.getContext(),CreateNoteActivity.class);
                intent.putExtra("id",holder.id);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}












