package com.tasko.tasky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class editNote extends AppCompatActivity implements View.OnClickListener{
    private EditText titleText;
    private EditText dateText;
    private EditText description;
    private Button update;
    private Button delete;
    private long _id;
    private NotesHelper notesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Note");
        setContentView(R.layout.editnote);
        notesHelper=new NotesHelper(this);
        notesHelper.open();
        titleText =findViewById(R.id.subject);
        dateText =findViewById(R.id.date);
        description =findViewById(R.id.description);
        update=findViewById(R.id.update);
        delete=findViewById(R.id.delete);

        Intent intent=getIntent();
        String id= intent.getStringExtra("_id");
        String name= intent.getStringExtra("title");
        String description= intent.getStringExtra("desc");
        String date= intent.getStringExtra("date");

        _id=Long.parseLong(id);
        titleText.setText(name);
        this.description.setText(description);
        this.dateText.setText(date);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.update) {
            String title = titleText.getText().toString();
            String desc = description.getText().toString();
            String date = dateText.getText().toString();
            notesHelper.update(_id, title, desc, date);
            this.returnHome();
        } else if (v.getId()==R.id.delete) {
            notesHelper.delete(_id);
            this.returnHome();
        }
        }


    public void returnHome(){
        Intent home_intent= new Intent(getApplicationContext(),dashboard.class).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
