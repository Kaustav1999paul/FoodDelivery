package com.example.fooddelivery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.CartModel;
import Model.OrderModel;
import ViewHolder.CartViewHolder;
import ViewHolder.OrderViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    public OrderFragment() {
        // Required empty public constructor
    }


    private RecyclerView cartList;
    private DatabaseReference OrderReff;
    private FirebaseUser user;
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();

        OrderReff = FirebaseDatabase.getInstance().getReference().child("Orders").child(id);

        cartList = view.findViewById(R.id.orderList);
        cartList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartList.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<OrderModel> options = new FirebaseRecyclerOptions.Builder<OrderModel>()
                .setQuery(OrderReff, OrderModel.class).build();

        FirebaseRecyclerAdapter<OrderModel, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<OrderModel, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final OrderModel model) {

                holder.personOrderName.setText(model.getStatus());
                holder.orderPrice.setText("₹"+model.getPrice());

                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderReff.child(model.getOrderId()).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent,false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
            }
        };
        cartList.setAdapter(adapter);
        adapter.startListening();

    }
}
