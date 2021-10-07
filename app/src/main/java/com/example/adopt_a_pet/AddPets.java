package com.example.adopt_a_pet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddPets extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mChooseImage;
    private Button mUploadImage;
    private EditText mPetName;
    private EditText mPetAge;
    private EditText mPetDesc;
    private ImageView mImageView;
    private ProgressBar pBar;
    private String ap = "ADOPT";

    private Uri mImageUri;
    StorageReference storage;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_pets);


        mImageView = findViewById(R.id.imageView);
        mChooseImage = findViewById(R.id.IDchooseFile);
        mUploadImage = findViewById(R.id.IDupload);
        mPetName = findViewById(R.id.IDpetName);
        mPetAge = findViewById(R.id.IDpetAge);
        mPetDesc = findViewById(R.id.IDpetDesc);
        pBar = findViewById(R.id.progressBar);


        storage =FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");


        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });



        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mPetName.getText().toString().trim()))
                {
                    mPetName.setError("Enter Name");
                    return;
                }
                if(TextUtils.isEmpty(mPetAge.getText().toString().trim()))
                {
                    mPetAge.setError("Enter Age");
                    return;
                }
                if(TextUtils.isEmpty(mPetDesc.getText().toString().trim())) {
                    mPetDesc.setError("Enter Description");
                    return;
                }
                uploadImage();
                pBar.setVisibility(View.VISIBLE);
            }

        } );





    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage()
    {
        if (mImageUri != null) {

            StorageReference reference = storage.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pBar.setVisibility(View.INVISIBLE);
                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String url = task.getResult().toString();
                            Upload upload = new Upload(mPetName.getText().toString(),url,
                                    mPetAge.getText().toString(),mPetDesc.getText().toString(),ap);
                            String uploadID = databaseReference.push().getKey();
                            databaseReference.child(uploadID).setValue(upload);
                            Toast.makeText(AddPets.this, "Pet Added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddPets.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }


    }





    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null)
                    {
                        mImageView.setImageURI(result);
                        mImageUri = result;
                    }
                }
            });
};