package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView personOrderName, orderPrice;
    public FloatingActionButton cancel;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderPrice = itemView.findViewById(R.id.orderPrice);
        personOrderName = itemView.findViewById(R.id.personOrderName);
        cancel = itemView.findViewById(R.id.cancel);


    }
}
