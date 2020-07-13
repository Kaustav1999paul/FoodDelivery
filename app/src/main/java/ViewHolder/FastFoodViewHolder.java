package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FastFoodViewHolder extends RecyclerView.ViewHolder {

    public TextView FoodName, price;
    public ImageView FoodImage;
    public FloatingActionButton add;
    public Spinner qty;

    public FastFoodViewHolder(@NonNull View itemView) {
        super(itemView);

        qty = itemView.findViewById(R.id.qty);
        FoodName = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        FoodImage = itemView.findViewById(R.id.foodImage);
        add = itemView.findViewById(R.id.add);
    }
}
