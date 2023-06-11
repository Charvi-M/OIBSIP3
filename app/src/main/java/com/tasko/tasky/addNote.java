package com.tasko.tasky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class addNote extends AppCompatActivity implements View.OnClickListener{
private Button add_note;
    FirebaseAuth mAuth;
    FirebaseUser user;
private EditText subject;
private EditText dateText;
private EditText description;
private DbManager manager;
private NotesHelper notesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Note");
        setContentView(R.layout.add_note);
        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        subject = findViewById(R.id.subject);
        dateText = findViewById(R.id.date);
        description = findViewById(R.id.description);
        add_note=findViewById(R.id.add_record);
        notesHelper=new NotesHelper(this);
        manager= new DbManager(this, "userdb", null, 1);
        notesHelper.open();
        add_note.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_record){
             final String email = user.getEmail();
             final String name = subject.getText().toString();
             final String desc = description.getText().toString();
             final String date = dateText.getText().toString();
             notesHelper.insert(name,desc,date,email);
                Intent main = new Intent(addNote.this, dashboard.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
        }
    }
}