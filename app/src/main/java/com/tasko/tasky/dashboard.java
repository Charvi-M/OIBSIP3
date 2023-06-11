package com.tasko.tasky;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class dashboard extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;

    private NotesHelper notesHelper;
    private ListView tasklist;
    private SimpleCursorAdapter adapter;
    final String[] from = new String[]{DbManager._id, DbManager.SUBJECT,DbManager.DESC, DbManager.DATE};
    final int[] to = new int[]{R.id._id, R.id.tasktitle, R.id.description, R.id.date};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        tasklist=findViewById(R.id.tasklist);
        DbManager manager = new DbManager(this, "userdb", null, 1);
        String email=user.getEmail();
        String uname = manager.getUserName(email);
        manager.close();
        notesHelper= new NotesHelper(this);
        notesHelper.open();
        Cursor cursor = notesHelper.fetch(email);
        notesHelper.close();
        TextView username = findViewById(R.id.username);
        if(user==null){
            Intent intent = new Intent(dashboard.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            username.setText("Hi "+uname);
        }

        ImageView bell = findViewById(R.id.bell);
        EditText search= findViewById(R.id.search);
        ListView tasklist=findViewById(R.id.tasklist);
        Button addtask= findViewById(R.id.button);

        tasklist.setEmptyView(findViewById(R.id.empty));
        adapter = new SimpleCursorAdapter(this, R.layout.view_note, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        tasklist.setAdapter(adapter);

        tasklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewid) {
                TextView id = findViewById(R.id._id);
                TextView titleText = findViewById(R.id.tasktitle);
                TextView description = findViewById(R.id.description);
                TextView dateText = findViewById(R.id.date);

                String _id;
                _id = id.getText().toString();
                String title=titleText.getText().toString();
                String desc=description.getText().toString();
                String date=dateText.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), editNote.class);
                modify_intent.putExtra("title",title);
                modify_intent.putExtra("desc",desc);
                modify_intent.putExtra("date",date);
                modify_intent.putExtra("_id",_id);

                startActivity(modify_intent);
            }
        });

                addtask.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(dashboard.this, addNote.class);
                     startActivity(intent);
                     finish();
                 }
        });




        Button LogOut = findViewById(R.id.LogOut);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(dashboard.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int _id=item.getItemId();
        if(_id==R.id.add_record){
            Intent add_mem = new Intent(this, addNote.class);
            startActivity(add_mem);
        }
        return super.onOptionsItemSelected(item);
    }
}
