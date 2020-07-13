package com.example.fooddelivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.FastFoodModel;
import ViewHolder.FListViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private CircleImageView avatar;
    private LinearLayout searchbar, ff,biryani,pizza,chinese;
    private TextView headingName, headPizza, headBir, headFast;
    private RecyclerView fList;
    private DatabaseReference FoodList, CartReff;
    private FirebaseUser user;
    private String userID, userName, count;
    private GifImageView loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        userName = user.getDisplayName();

        loading = view.findViewById(R.id.loading);
        CartReff = FirebaseDatabase.getInstance().getReference().child("Cart").child(userID);
        FoodList = FirebaseDatabase.getInstance().getReference().child("FoodList");
        headingName = view.findViewById(R.id.chi);
        headPizza = view.findViewById(R.id.piz);
        headBir = view.findViewById(R.id.headBir);
        headFast = view.findViewById(R.id.headFast);
        fList = view.findViewById(R.id.fList);
        avatar = view.findViewById(R.id.avatar);
        searchbar = view.findViewById(R.id.searchbarr);
        ff = view.findViewById(R.id.fastf);
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                ActivityOptionsCompat optionsCompa = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), headFast, ViewCompat.getTransitionName(headFast));
                startActivity(intent, optionsCompa.toBundle());
            }
        });

        biryani = view.findViewById(R.id.biryani);
        biryani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BiryaniActivity.class);
                ActivityOptionsCompat optionsCompa = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), headBir, ViewCompat.getTransitionName(headBir));
                startActivity(intent, optionsCompa.toBundle());
            }
        });

        pizza = view.findViewById(R.id.pizza);
        pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PizzaActivity.class);
                ActivityOptionsCompat optionsCompa = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), headPizza, ViewCompat.getTransitionName(headPizza));
                startActivity(intent, optionsCompa.toBundle());
            }
        });

        chinese = view.findViewById(R.id.chinese);
        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChineseActivity.class);
                ActivityOptionsCompat optionsCompa = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), headingName, ViewCompat.getTransitionName(headingName));
                startActivity(intent, optionsCompa.toBundle());
            }
        });
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                ActivityOptionsCompat optionsCompa = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), searchbar, ViewCompat.getTransitionName(searchbar));
                startActivity(intent, optionsCompa.toBundle());
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            final String user_name = user.getDisplayName();
            final String email = user.getEmail();
            final Uri photoUrl = user.getPhotoUrl();
            String user_uid = user.getUid();
            Picasso.get().load(photoUrl).into(avatar);
        }

        fList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        fList.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<FastFoodModel> options = new FirebaseRecyclerOptions.Builder<FastFoodModel>()
                .setQuery(FoodList, FastFoodModel.class).build();

        FirebaseRecyclerAdapter<FastFoodModel, FListViewHolder> adapter = new FirebaseRecyclerAdapter<FastFoodModel, FListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FListViewHolder holder, int position, @NonNull FastFoodModel model) {

                loading.setVisibility(View.GONE);
                fList.setVisibility(View.VISIBLE);
                holder.cartName.setText(model.getFoodName());
                holder.cartPrice.setText("₹"+model.getPrice());
                Glide.with(holder.cartImage).load(model.getProduct_Image()).into(holder.cartImage);

                final String fName, fPrice, fCategory, fImage, fId,saveCurrentDate, saveCurrentTime, ProductRandomKey;
                fName = model.getFoodName();
                fCategory = model.getCategory();
                fPrice = model.getPrice();
                fImage = model.getProduct_Image();
                fId = model.getFoodid();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
                saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calendar.getTime());
                ProductRandomKey = fId + saveCurrentDate + saveCurrentTime;

                holder.addd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartReff.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!(dataSnapshot.child("Cart").child(userID).child(ProductRandomKey).exists())){
                                    HashMap<String, Object> productMap = new HashMap<>();
                                    productMap.put("foodName" ,fName);
                                    productMap.put("foodid" ,fId);
                                    productMap.put("date" , saveCurrentDate);
                                    productMap.put("time" ,saveCurrentTime);
                                    productMap.put("Product_Image" , fImage);
                                    productMap.put("category" ,  fCategory);
                                    productMap.put("price" ,fPrice);
                                    productMap.put("cartId", ProductRandomKey);
                                    productMap.put("userId" ,userID);
                                    productMap.put("userName" ,userName);
                                    productMap.put("count" ,"1");

                                    CartReff.child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                holder.addd.setImageResource(R.drawable.ic_check);
                                                holder.addd.setEnabled(false);
                                            }else {

                                                String mess = task.getException().toString();
                                                Toast.makeText(getContext(), "Error: " + mess, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public FListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_food_list_layout, parent,false);
                FListViewHolder holder = new FListViewHolder(view);
                return holder;

            }
        };
        fList.setAdapter(adapter);
        adapter.startListening();

    }
}
