package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public ImageView cartImage;
    public TextView cartName, cartPrice;
    public FloatingActionButton delete;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartName = itemView.findViewById(R.id.cartName);
        cartImage = itemView.findViewById(R.id.cartImage);
        cartPrice = itemView.findViewById(R.id.cartPrice);
        delete = itemView.findViewById(R.id.remove);
    }
}
