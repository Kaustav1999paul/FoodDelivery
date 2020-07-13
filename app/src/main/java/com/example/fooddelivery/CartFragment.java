package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.CartModel;
import ViewHolder.CartViewHolder;


public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartList;
    private DatabaseReference CartReff;
    private FirebaseUser user;
    String id;
    private TextView temp;
    private int overTotalPrice = 0;
    private Button pay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        temp = view.findViewById(R.id.temp);
        user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();
        pay = view.findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (overTotalPrice == 0){
                    Toast.makeText(getContext(), "Cart should not be empty to place order.", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
                    intent.putExtra("Total price: ", String.valueOf(overTotalPrice));
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        CartReff = FirebaseDatabase.getInstance().getReference().child("Cart").child(id);

        cartList = view.findViewById(R.id.cartList);
        cartList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartList.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<CartModel> options = new FirebaseRecyclerOptions.Builder<CartModel>()
                .setQuery(CartReff, CartModel.class).build();

        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final CartModel model) {

                temp.setVisibility(View.INVISIBLE);
                holder.cartName.setText(model.getFoodName());
                holder.cartPrice.setText("₹"+model.getPrice());
                Glide.with(holder.cartImage).load(model.getProduct_Image()).into(holder.cartImage);

                int oneTypeProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getCount());
                overTotalPrice = oneTypeProductTPrice + overTotalPrice;

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartReff.child(model.getCartId()).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        cartList.setAdapter(adapter);
        adapter.startListening();

    }
}
