package com.avishkar.mini_e_commerse.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avishkar.mini_e_commerse.R;

public class CartHolder extends RecyclerView.ViewHolder {

    public ImageView cartImage;
    public TextView tvCartItem, tvTaxPrice, tvDiscountPrice, tvActualPrice;
    public ImageButton removeIb;
    public CartHolder(@NonNull View itemView) {
        super(itemView);
        cartImage = itemView.findViewById(R.id.cartImage);
        tvCartItem = itemView.findViewById(R.id.tvItemName);
        tvTaxPrice = itemView.findViewById(R.id.tvTaxPrice);
        tvDiscountPrice = itemView.findViewById(R.id.tvCartDiscountPrice);
        tvActualPrice = itemView.findViewById(R.id.tvCartActualPrice);
        removeIb = itemView.findViewById(R.id.btnRemove);
    }
}
