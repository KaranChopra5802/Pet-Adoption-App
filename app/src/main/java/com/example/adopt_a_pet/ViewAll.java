package com.example.adopt_a_pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewAll extends AppCompatActivity {

    private TextView name;
    private TextView age;
    private TextView desc;
    private List<Upload> mUploads;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        name = findViewById(R.id.Name);
        age = findViewById(R.id.Age);
        desc = findViewById(R.id.Description);

        Intent intent = getIntent();
        int position = intent.getIntExtra("POS",0);


        //Toast.makeText(this, position, Toast.LENGTH_SHORT).show();

        //Upload uploadCurrent = mUploads.get(1);
        //name.setText(uploadCurrent.getmPetName());
        //age.setText(uploadCurrent.getmPetAge());
        //desc.setText(uploadCurrent.getmPetDesc());



    }
}