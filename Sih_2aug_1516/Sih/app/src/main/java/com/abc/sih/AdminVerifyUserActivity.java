package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminVerifyUserActivity extends AppCompatActivity {

    DatabaseReference ref;
    ArrayList<Doctor> list;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verify_user);

        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ref !=null)
        {
            ref.orderByChild("verified_user").equalTo("no").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        list = new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            list.add(ds.getValue(Doctor.class));

                        }
                        AdapterClass adapterClass = new AdapterClass(list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminVerifyUserActivity.this));
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(AdminVerifyUserActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(searchView !=null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String str)
    {
        ArrayList<Doctor> myList = new ArrayList<>();
        for(Doctor object : list)
        {
            if(object.getName().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(myList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminVerifyUserActivity.this));
        recyclerView.setAdapter(adapterClass);
    }

    public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

        ArrayList<Doctor> list;
        public AdapterClass(ArrayList<Doctor> list)
        {
            this.list = list;
        }

        @NonNull
        @Override
        public AdapterClass.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_officer_attd_try, viewGroup, false);

            return new AdapterClass.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterClass.MyViewHolder myViewHolder, final int i) {

            if (i <list.size()){
                myViewHolder.id.setText(list.get(i).getName());
                String mcode;
                if (list.get(i).getType().equals("D")){
                    mcode = "Divisional Officer";
                }else if(list.get(i).getType().equals("R")){
                    mcode = "Range Officer";
                }else if (list.get(i).getType().equals("B")){
                    mcode = "Beat Officer";
                }else {
                    mcode = "Forest Guard";
                }
                myViewHolder.desc.setText(mcode);
                String abc = list.get(i).getImage();
                Picasso.with(AdminVerifyUserActivity.this).load(abc).placeholder(R.color.grey).networkPolicy(NetworkPolicy.OFFLINE).into(myViewHolder.profile_img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(AdminVerifyUserActivity.this).load(abc).placeholder(R.color.grey).into(myViewHolder.profile_img);
                    }
                });

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminVerifyUserActivity.this,ProfileActivityNakli.class);
                        intent.putExtra("user_id",list.get(i).getId());
                        Toast.makeText(AdminVerifyUserActivity.this,list.get(i).getId(),Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });

                myViewHolder.verify_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ref.child(list.get(i).getId()).child("verified_user").setValue("yes");
                        //Toast.makeText(AdminVerifyUserActivity.this, list.get(i).getId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView id,desc;
            CircleImageView profile_img;
            Button verify_btn;
            // RatingBar ratingBar;
            // TextView ratetext;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.docId);
                desc = itemView.findViewById(R.id.description);
                profile_img = itemView.findViewById(R.id.doc_profile);
                verify_btn = itemView.findViewById(R.id.verify_btn);
            }
        }
    }
}