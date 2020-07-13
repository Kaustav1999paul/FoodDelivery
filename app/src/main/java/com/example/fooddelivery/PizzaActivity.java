package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Model.FastFoodModel;
import ViewHolder.FastFoodViewHolder;

public class PizzaActivity extends AppCompatActivity {

    private RecyclerView foodList;
    private DatabaseReference FastFoodReff, CartReff;
    private FirebaseUser user;
    private String userID, userName, count;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        userName = user.getDisplayName();

        FastFoodReff = FirebaseDatabase.getInstance().getReference().child("Foods").child("Pizza");
        CartReff = FirebaseDatabase.getInstance().getReference().child("Cart").child(userID);

        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        foodList.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<FastFoodModel> options = new FirebaseRecyclerOptions.Builder<FastFoodModel>()
                .setQuery(FastFoodReff, FastFoodModel.class)
                .build();

        FirebaseRecyclerAdapter<FastFoodModel, FastFoodViewHolder> adapter = new FirebaseRecyclerAdapter<FastFoodModel, FastFoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FastFoodViewHolder holder, int position, @NonNull final FastFoodModel model) {

                holder.FoodName.setText(model.getFoodName());
                holder.price.setText("₹"+model.getPrice());
                Glide.with(holder.FoodImage).load(model.getProduct_Image()).into(holder.FoodImage);

                final String fName, fPrice, fCategory, fImage, fId,saveCurrentDate, saveCurrentTime, ProductRandomKey;
                fName = model.getFoodName();
                fCategory = model.getCategory();
                fPrice = model.getPrice();
                fImage = model.getProduct_Image();
                fId = model.getFoodid();

                holder.qty.setAdapter(arrayAdapter);
                holder.qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        count = list.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
                saveCurrentDate = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calendar.getTime());
                ProductRandomKey = fId + saveCurrentDate + saveCurrentTime;

                holder.add.setOnClickListener(new View.OnClickListener() {
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
                                    productMap.put("count" ,count);

                                    CartReff.child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                holder.add.setImageResource(R.drawable.ic_check);
                                                holder.add.setEnabled(false);
                                            }else {

                                                String mess = task.getException().toString();
                                                Toast.makeText(PizzaActivity.this, "Error: " + mess, Toast.LENGTH_SHORT).show();
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
            public FastFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,false);
                FastFoodViewHolder holder = new FastFoodViewHolder(view);
                return holder;
            }
        };
        foodList.setAdapter(adapter);
        adapter.startListening();
    }
}
