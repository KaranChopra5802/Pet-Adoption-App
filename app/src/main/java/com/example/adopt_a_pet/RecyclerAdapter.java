package com.example.adopt_a_pet;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ViewHolder>

{



    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;

    public RecyclerAdapter(Context context, List<Upload> uploads)
    {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Upload uploadCurrent = mUploads.get(position);
        holder.name.setText(uploadCurrent.getmPetName());
        holder.age.setText(uploadCurrent.getmPetAge());
        holder.adopt.setText(uploadCurrent.getMadopt());

        Picasso.get().load(uploadCurrent.getmImageURL()).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(Exception e) {

            }
        });

        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener
    {
        public TextView name;
        public TextView age;
        public ImageView imageView;
        public Button adopt;
        public ProgressBar progressBar;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.IDname);
            age = itemView.findViewById(R.id.IDage);
            imageView = itemView.findViewById(R.id.imageView2);
            adopt = itemView.findViewById(R.id.Adopt);
            progressBar = itemView.findViewById(R.id.progressBar2);
            ArrayList<String> muid = new ArrayList<String>();




            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            final boolean[] isSelected = {true};

            adopt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mAuth.getCurrentUser() == null)
                    {

                    }
                    else {
                        String check = adopt.getText().toString();
                        Log.d("POS", adopt.getText().toString());
                        if(isSelected[0]){

                            int position = getAdapterPosition();
                            Log.d("POS", adopt.getText().toString());

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads");

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                        String uid = datas.getKey();
                                        muid.add(uid);

                                    }
                                    String key = muid.get(position);
                                    databaseReference.child(key).child("madopt").setValue("ADOPT");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else if (!isSelected[0]) {

                            int position = getAdapterPosition();
                            Log.d("POSA", String.valueOf(position));

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads");

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                        String uid = datas.getKey();
                                        muid.add(uid);

                                    }
                                    String key = muid.get(position);
                                    databaseReference.child(key).child("madopt").setValue("PENDING");;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        isSelected[0] = !isSelected[0];
                    }

                }
            });
        }

        @Override
        public void onClick(View v) {
            if(mListener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem Delete = menu.add(Menu.NONE, 1,1,"Delete Pet Info");
            Delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                   switch(item.getItemId())
                   {
                       case 1:
                           mListener.onDeleteClick(position);
                           return true;
                   }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
            mListener = listener;
    }
}
