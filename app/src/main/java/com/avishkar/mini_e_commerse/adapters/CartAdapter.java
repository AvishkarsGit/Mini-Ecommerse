package com.avishkar.mini_e_commerse.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avishkar.mini_e_commerse.MainActivity;
import com.avishkar.mini_e_commerse.R;
import com.avishkar.mini_e_commerse.activities.CartActivity;
import com.avishkar.mini_e_commerse.constants.Global;
import com.avishkar.mini_e_commerse.database.DatabaseHelper;
import com.avishkar.mini_e_commerse.holders.CartHolder;
import com.avishkar.mini_e_commerse.models.ProductsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartHolder> {

    private ArrayList<ProductsModel> cartList;
    private Context context;
    private DatabaseHelper helper;
    private CartActivity cartActivity;

    public CartAdapter(Context context, ArrayList<ProductsModel> cartList, CartActivity cartActivity) {
        this.context = context;
        this.cartList = cartList;
        helper = new DatabaseHelper(context);
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items_layout,parent,false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        ProductsModel model = cartList.get(position);

        holder.tvCartItem.setText(model.getProductName());
        holder.tvActualPrice.setText("₹" + model.getProductActualPrice() + ".00");
        holder.tvTaxPrice.setText(model.getProductTax() + "% Tax");

        if (model.isDiscount()) {
            holder.tvDiscountPrice.setVisibility(View.VISIBLE);
            holder.tvDiscountPrice.setText("₹" + model.getProductDiscountPrice() + ".00");
            holder.tvDiscountPrice.setPaintFlags(
                    holder.tvDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.tvDiscountPrice.setVisibility(View.GONE);
            holder.tvDiscountPrice.setPaintFlags(
                    holder.tvDiscountPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
            );
        }

        //cancel previous image request
        Picasso.get().cancelRequest(holder.cartImage);

        // image loading
        Picasso.get()
                .load(model.getProductImg())
                .placeholder(R.drawable.placeholder_img)
                .fit()
                .centerCrop()
                .into(holder.cartImage);

        //handle click remove from cart
        holder.removeIb.setOnClickListener(v -> {
            // remove from cart logic
            boolean isUpdated = helper.addAndRemoveItemToCart(model.getId(),false);
            if (isUpdated) {
                cartActivity.loadCartItems();
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
