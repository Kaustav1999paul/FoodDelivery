package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmOrderActivity extends AppCompatActivity {

    private String totalAmount = "";
    private TextView price, PersonName, PersonEmail;
    private CircleImageView ava;
    private FirebaseUser user;
    private FloatingActionButton confirm;
    private String status="Ordered", saveCurrentDate, saveCurrentTime, ProductRandomKey;
    private DatabaseReference OrderReff,CartReff;
    private Dialog accountDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        accountDialog = new Dialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        OrderReff = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getUid());
        CartReff = FirebaseDatabase.getInstance().getReference().child("Cart").child(user.getUid());

        ava = findViewById(R.id.ava);
        PersonName = findViewById(R.id.personName);
        PersonEmail = findViewById(R.id.personEmail);
        confirm = findViewById(R.id.placeOrder);

        PersonEmail.setText(user.getEmail());
        PersonName.setText(user.getDisplayName());
        Glide.with(ava).load(user.getPhotoUrl()).into(ava);

        totalAmount = getIntent().getStringExtra("Total price: ");
        price = findViewById(R.id.amount);
        price.setText("₹"+totalAmount);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        ProductRandomKey = saveCurrentDate + saveCurrentTime;

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOrder();
            }
        });
    }

    private void PlaceOrder() {
        OrderReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Orders").child(user.getUid()).child(ProductRandomKey).exists())){
                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("date" , saveCurrentDate);
                    productMap.put("time" ,saveCurrentTime);
                    productMap.put("price" ,totalAmount);
                    productMap.put("orderId", ProductRandomKey);
                    productMap.put("status", status);

                    OrderReff.child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                showCustomDialog();
                                CartReff.removeValue();
                            }else {

                                String mess = task.getException().toString();
                                Toast.makeText(ConfirmOrderActivity.this, "Error: " + mess, Toast.LENGTH_SHORT).show();
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

    private void showCustomDialog() {
        accountDialog.setContentView(R.layout.confirm_layout);
        accountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        accountDialog.setCanceledOnTouchOutside(false);
        accountDialog.show();

        Button b = accountDialog.findViewById(R.id.ok);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
