package com.example.yourquerybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView fname,em,username,userType;
    Button logout;
//    FloatingActionButton addEmployee;
    FirebaseUser user;
    DatabaseReference usersReference;
    String uSertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        fname=findViewById(R.id.fname);
        em=findViewById(R.id.em);
        username=findViewById(R.id.user);
        logout=findViewById(R.id.logout);
        userType=findViewById(R.id.userType);
//        addEmployee=findViewById(R.id.add);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String first_name = snapshot.child(uid).child("firstName").getValue(String.class);
                String last_name = snapshot.child(uid).child("lastName").getValue(String.class);
                String Email = snapshot.child(uid).child("email").getValue(String.class);
                String userName = snapshot.child(uid).child("username").getValue(String.class);
                uSertype = snapshot.child(uid).child("userType").getValue(String.class);
//                if(uSertype.equals("Admin")){
//                    addEmployee.setVisibility(View.VISIBLE);
//                }
                fname.setText(first_name + " " + last_name);
                em.setText(Email);
                username.setText(userName);
                userType.setText(uSertype);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finish();
            }
        });
//        addEmployee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this,RegisterActivity.class));
//                finish();
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
        finish();
    }
}