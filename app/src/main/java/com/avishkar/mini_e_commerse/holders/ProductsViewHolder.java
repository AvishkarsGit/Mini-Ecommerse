package com.avishkar.mini_e_commerse.holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avishkar.mini_e_commerse.R;

public class ProductsViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName,tvActualPrice,tvDiscountPrice, tvTax;
    public ImageView productImg;
    public  Button btnAddToCart, btnRemoveCart;

    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvActualPrice = itemView.findViewById(R.id.tvActualPrice);
        tvDiscountPrice = itemView.findViewById(R.id.tvDiscountPrice);
        tvTax = itemView.findViewById(R.id.tvTax);
        productImg = itemView.findViewById(R.id.productImg);
        btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        btnRemoveCart = itemView.findViewById(R.id.btnRemoveCart);
    }
}
