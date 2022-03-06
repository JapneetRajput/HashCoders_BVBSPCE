package com.example.yourquerybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentPanel extends AppCompatActivity {
    Button addComment;
    DatabaseReference realtimeDB= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_panel);
        addComment = findViewById(R.id.buttonComment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(CommentPanel.this);
                dialog.setContentView(R.layout.comment);

                TextInputEditText comment=dialog.findViewById(R.id.comment);
                Button addComment = dialog.findViewById(R.id.addComment);
                addComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }
}