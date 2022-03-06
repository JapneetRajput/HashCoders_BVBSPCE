package com.example.yourquerybuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HomeActivity extends AppCompatActivity {
    ImageButton profile,noticeBoard, leave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        leave = findViewById(R.id.leave);
        Glide.with(this).load(R.drawable.discuss).into(leave);

        profile = findViewById(R.id.profile);
        Glide.with(this).load(R.drawable.profile).into(profile);

        noticeBoard = findViewById(R.id.notice);
        Glide.with(this).load(R.drawable.ongoing).into(noticeBoard);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LeaveActivity.class));
                finish();
            }
        });
        noticeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProjectActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
}