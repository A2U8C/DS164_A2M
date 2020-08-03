package com.abc.sih;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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


public class Frag5 extends Fragment {

    DatabaseReference ref;
    ArrayList<Doctor> list;
    RecyclerView recyclerView;
    SearchView searchView;
    public Frag5() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag5, container, false);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = view.findViewById(R.id.rv);
        searchView = view.findViewById(R.id.searchView);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ref !=null)
        {
            ref.addValueEventListener(new ValueEventListener() {
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
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_officer_nakli, viewGroup, false);

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
                Picasso.with(getContext()).load(abc).placeholder(R.color.grey).networkPolicy(NetworkPolicy.OFFLINE).into(myViewHolder.profile_img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(getContext()).load(abc).placeholder(R.color.grey).into(myViewHolder.profile_img);
                    }
                });
                //String rate = list.get(i).getRating();
                //myViewHolder.ratingBar.setRating(Float.parseFloat(rate));
                // final String num = list.get(i).getNumber_of_rating();
                // myViewHolder.ratetext.setText(rate + "(" +num + ")");
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),ProfileActivityNakli.class);
                        intent.putExtra("user_id",list.get(i).getId());
                        Toast.makeText(getContext(),list.get(i).getId(),Toast.LENGTH_SHORT).show();
                        startActivity(intent);
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
            // RatingBar ratingBar;
            // TextView ratetext;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.docId);
                desc = itemView.findViewById(R.id.description);
                profile_img = itemView.findViewById(R.id.doc_profile);
                // ratingBar = itemView.findViewById(R.id.rating);
                // ratetext = itemView.findViewById(R.id.ratingText);
            }
        }
    }

}
