package com.avishkar.mini_e_commerse.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.avishkar.mini_e_commerse.MainActivity;
import com.avishkar.mini_e_commerse.R;
import com.avishkar.mini_e_commerse.activities.CartActivity;
import com.avishkar.mini_e_commerse.constants.Global;
import com.avishkar.mini_e_commerse.database.DatabaseHelper;
import com.avishkar.mini_e_commerse.holders.ProductsViewHolder;
import com.avishkar.mini_e_commerse.models.ProductsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    private Context context;
    private ArrayList<ProductsModel> productsList;
    private MainActivity mainActivity;
    private DatabaseHelper helper;
    public ProductsAdapter(Context context, ArrayList<ProductsModel> productsList, MainActivity mainActivity) {
        this.context = context;
        this.productsList = productsList;
        this.mainActivity = mainActivity;
        helper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_layout,parent,false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        ProductsModel model = productsList.get(position);

        holder.tvName.setText(model.getProductName());
        holder.tvActualPrice.setText("₹" + model.getProductActualPrice() + ".00");
        holder.tvTax.setText(model.getProductTax() + "% Tax");

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

        //check if item already exist in the cart
        if (model.isCart()) {
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.btnRemoveCart.setVisibility(View.VISIBLE);
        }
        else {
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.btnRemoveCart.setVisibility(View.GONE);
        }

        //cancel previous image request
        Picasso.get().cancelRequest(holder.productImg);

        // image loading
        Picasso.get()
                .load(model.getProductImg())
                .placeholder(R.drawable.placeholder_img)
                .fit()
                .centerCrop()
                .into(holder.productImg);


        holder.btnAddToCart.setOnClickListener(v -> {
            // add to cart logic
            boolean isUpdated = helper.addAndRemoveItemToCart(model.getId(),true);
            if (isUpdated) {
                mainActivity.updateCartCount();
                holder.btnRemoveCart.setVisibility(View.VISIBLE);
                holder.btnAddToCart.setVisibility(View.GONE);
                //Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show();
                Global.showSuccessToast("Item added successfulyy..",context);
            }
        });

        holder.btnRemoveCart.setOnClickListener(v-> {
            // remove from cart logic
            boolean isUpdated = helper.addAndRemoveItemToCart(model.getId(),false);
            if (isUpdated) {
                mainActivity.updateCartCount();
                holder.btnRemoveCart.setVisibility(View.GONE);
                holder.btnAddToCart.setVisibility(View.VISIBLE);
                Global.showErrorToast("Item removed successfully..",context);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


}
