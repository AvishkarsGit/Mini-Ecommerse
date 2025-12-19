package com.avishkar.mini_e_commerse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avishkar.mini_e_commerse.activities.CartActivity;
import com.avishkar.mini_e_commerse.adapters.ProductsAdapter;
import com.avishkar.mini_e_commerse.constants.ProductConstants;
import com.avishkar.mini_e_commerse.database.DatabaseHelper;
import com.avishkar.mini_e_commerse.models.ProductsModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //Recycler view
    private RecyclerView productsRv;

    //Textview
    private TextView tvCartItemCount;
    private ImageButton cartIb;

    //products array list
    private ArrayList<ProductsModel> productsList;

    //database helper class
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize view widgets
        initWidgets();

        //check if table is empty or not to avoid duplicate data insertion each time.
        checkData();

        //load data from database
        loadData();

        //handle click on cart icon
        cartIb.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this, CartActivity.class);
            startActivity(i);
        });
    }

    private void checkData() {
        boolean isEmpty = helper.isTableEmpty();
        if(isEmpty) {
            //initialize data only if table is empty.
            initData();
        }
    }

    private void initData() {
        for (int i = 0; i<ProductConstants.clothes.length; i++) {
            boolean isDiscounted = i % 2 == 0;
            int tax = ( i % 2 == 0) ? 5 : 18;
            helper.addProduct(
                    ProductConstants.clothes[i],
                    ProductConstants.images[i],
                    ProductConstants.generatePrice(),
                    ProductConstants.generateDiscountPrice(),
                    isDiscounted,
                    false,
                    tax
            );
        }
    }

    private void loadData(){
        productsList = helper.getAllProducts();
        ProductsAdapter adapter = new ProductsAdapter(MainActivity.this, productsList, MainActivity.this);
        productsRv.setAdapter(adapter);

        //update cart count
        updateCartCount();
    }

    private void initWidgets() {
        //init helper class
        helper = new DatabaseHelper(MainActivity.this);
        tvCartItemCount = findViewById(R.id.tvItemsCount);
        cartIb = findViewById(R.id.cartIb);

        //init views
        productsRv = findViewById(R.id.productsRv);

        //init recycler view
        productsRv.setLayoutManager(new LinearLayoutManager(this));

        //init array list
        productsList = new ArrayList<>();

    }


    public void updateCartCount() {
        int count = helper.getCartCount();
        if (count <= 0) {
            tvCartItemCount.setVisibility(View.GONE);
        }
        else {
            tvCartItemCount.setVisibility(View.VISIBLE);
            tvCartItemCount.setText(String.valueOf(count));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }
}