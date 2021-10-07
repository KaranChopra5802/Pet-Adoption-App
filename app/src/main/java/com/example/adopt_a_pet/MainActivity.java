package com.example.adopt_a_pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Contacts.SettingsColumns.KEY;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

     private RecyclerAdapter recyclerAdapter;
     private RecyclerView recyclerView;
     private DatabaseReference databaseReference;
     private List<Upload> mUploads;
     private ImageView refresh;
     private FirebaseStorage mStorage;
     FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Long Press to Delete", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onDeleteClick(int position) {
        if(mAuth.getCurrentUser() == null)
        {
            Toast.makeText(this, "Login to delete info", Toast.LENGTH_SHORT).show();
        }
        if(mAuth.getCurrentUser() != null) {
            Upload selectedItem = mUploads.get(position);
            String SelectedKey = selectedItem.getmKey();

            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getmImageURL());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    databaseReference.child(SelectedKey).removeValue();
                    Toast.makeText(MainActivity.this, "Pet Info Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ImageView logout = findViewById(R.id.LogOut);
        FloatingActionButton AddPet2 = findViewById(R.id.floatingActionButton);


        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),VERTICAL);

        refresh = findViewById(R.id.reloadView);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Refreshing Page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        AddPet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()==null)
                {
                    Toast.makeText(MainActivity.this, "Login to add Pets", Toast.LENGTH_SHORT).show();
                }
                if(mAuth.getCurrentUser() !=null) {
                    Intent intent = new Intent(MainActivity.this, AddPets.class);
                    startActivity(intent);
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();

            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(itemDecoration);

        mUploads = new ArrayList<>();

        recyclerAdapter = new RecyclerAdapter(MainActivity.this, mUploads);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(MainActivity.this);

        mStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUploads.clear();
                for(DataSnapshot postSnapShot : snapshot.getChildren())
                {
                    Upload upload = postSnapShot.getValue(Upload.class);
                    upload.setmKey(postSnapShot.getKey());
                    mUploads.add(upload);
                }

                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


}