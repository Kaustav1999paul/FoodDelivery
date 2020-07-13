package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FListViewHolder extends RecyclerView.ViewHolder {

    public ImageView cartImage;
    public TextView cartName, cartPrice;
    public FloatingActionButton addd;

    public FListViewHolder(@NonNull View itemView) {
        super(itemView);

        cartName = itemView.findViewById(R.id.hName);
        cartImage = itemView.findViewById(R.id.hImage);
        cartPrice = itemView.findViewById(R.id.hPrice);
        addd = itemView.findViewById(R.id.cartAdd);

    }
}
