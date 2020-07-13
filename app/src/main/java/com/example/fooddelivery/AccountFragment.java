package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }

    private CircleImageView avat;
    private TextView na, em, co, sta;
    private FirebaseUser user;
    private DatabaseReference Reff;
    int count;
    private FloatingActionButton logout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Reff = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getUid());

        mAuth = FirebaseAuth.getInstance();
        logout = view.findViewById(R.id.logout);
        na = view.findViewById(R.id.Aname);
        em = view.findViewById(R.id.Aemail);
        co = view.findViewById(R.id.co);
        sta = view.findViewById(R.id.status);
        avat = view.findViewById(R.id.avat);

        na.setText(user.getDisplayName());
        em.setText(user.getEmail());
        Glide.with(avat).load(user.getPhotoUrl()).into(avat);

        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                count = (int) dataSnapshot.getChildrenCount();
                co.setText(Integer.toString(count));

                if (count <= 3){
                    sta.setText("Gold");
                    sta.setTextColor(getResources().getColor(R.color.gold));
                }else if (count >=4 && count <= 6){
                    sta.setText("Platinum");
                    sta.setTextColor(getResources().getColor(R.color.plat));
                }else {
                    sta.setText("Diamond");
                    sta.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                    startActivity(intent);
                }
            }
        };
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
