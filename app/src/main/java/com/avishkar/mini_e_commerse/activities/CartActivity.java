package com.avishkar.mini_e_commerse.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avishkar.mini_e_commerse.R;
import com.avishkar.mini_e_commerse.adapters.CartAdapter;
import com.avishkar.mini_e_commerse.constants.Global;
import com.avishkar.mini_e_commerse.interfaces.OnPaymentComplete;
import com.avishkar.mini_e_commerse.database.DatabaseHelper;
import com.avishkar.mini_e_commerse.models.ProductsModel;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {


    //widgets
    private ImageButton backIb;
    private RecyclerView cartRv;
    private TextView tvItemsCount, tvApplyCoupon, tvTotalPrice, tvTaxPrice, tvFinalAmt;
    private LinearLayout cartLayout,emptyCartLayout;
    private Button btnCheckout;

    //ArrayList
    private ArrayList<ProductsModel> cartList;
    //database
    private DatabaseHelper helper;

    //variable
    String textItems,textTotalPrice,textTaxPrice,textFinalAmt, textButtonPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize widgets and classes
        initWidgets();

        //handle click on back button
        backIb.setOnClickListener(v-> {
             finish();
        });

        //load cart items
        loadCartItems();

        //handle click on apply coupon code text
        tvApplyCoupon.setOnClickListener(v -> {
            applyCouponCode();
        });

        //handle click on check-out
        btnCheckout.setOnClickListener(v-> {

            Global.showLoader(CartActivity.this);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Global.hideLoader(CartActivity.this);
                    Global.showPaymentSuccess(CartActivity.this, new OnPaymentComplete() {
                        @Override
                        public void onSuccess() {
                            removeItemsFromCart();
                        }
                    });
                }
            },3000);

        });


    }

    private void removeItemsFromCart() {
        helper.removeItemsFromCart();
        loadCartItems();
    }

    private void applyCouponCode() {
        if (!cartList.isEmpty()) {
            //check if without coupon cart total is greater than 1000 or not
            if (getFinalAmount(false) > 1000 ) {
                //check if all products are already have discount
                if (isAllProductsDiscounted()){
                    Global.showWarningToast("Discount is already applied!...",CartActivity.this);
                }
                else {
                    updateUI(cartList.size(),getTotalPrice(true),getTotalTaxAmount(true),getFinalAmount(true));
                    tvApplyCoupon.setTextColor(Color.GRAY);
                    tvApplyCoupon.setEnabled(false);
                    Global.showSuccessToast("Coupon apply successfully..",CartActivity.this);
                }
            }
            else {
                Global.showWarningToast("Products total must be more than ₹1000",CartActivity.this);
            }
        }
    }

    private boolean isAllProductsDiscounted() {
        for (int i = 0; i<cartList.size();i++) {
            if (!cartList.get(i).isDiscount()) {
                return false;
            }
        }
        return true;
    }

    private void updateUI(int totalCartItems,double totalPrice, double totalTaxPrice,double totalFinalAmt) {
        //calculate price;

        if (totalCartItems > 0) {

            textItems = totalCartItems+" Items";
            textTotalPrice ="₹"+String.format("%.2f",totalPrice);
            textTaxPrice = "₹"+ String.format("%.2f",totalTaxPrice);
            textFinalAmt = "₹"+ String.format("%.2f",totalFinalAmt);
            textButtonPrice = "Pay ₹"+ String.format("%.2f",totalFinalAmt);

            tvItemsCount.setText(textItems);
            tvTotalPrice.setText(textTotalPrice);
            tvTaxPrice.setText(textTaxPrice);
            tvFinalAmt.setText(textFinalAmt);
            btnCheckout.setText(textButtonPrice);
        }

    }

    public void loadCartItems() {
        cartList = helper.getCartItems();
        if (!cartList.isEmpty()) {
            cartLayout.setVisibility(View.VISIBLE);
            emptyCartLayout.setVisibility(View.GONE);
            CartAdapter adapter = new CartAdapter(this,cartList,CartActivity.this);
            cartRv.setAdapter(adapter);

            tvApplyCoupon.setTextColor(getResources().getColor(R.color.primary));
            tvApplyCoupon.setEnabled(true);

            //update ui
            updateUI(cartList.size(),getTotalPrice(false),getTotalTaxAmount(false),getFinalAmount(false));
        }
        else {
            emptyCartLayout.setVisibility(View.VISIBLE);
            cartLayout.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
        }

    }

    private void initWidgets() {
        //database helper
        helper =new DatabaseHelper(this);

        //views
        backIb = findViewById(R.id.backIb);
        cartRv = findViewById(R.id.cartItemsRv);
        tvItemsCount = findViewById(R.id.tvTotalItems);
        tvApplyCoupon = findViewById(R.id.tvApplyCoupon);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTaxPrice = findViewById(R.id.tvTotalTax);
        tvFinalAmt = findViewById(R.id.tvFinalPrice);
        cartLayout = findViewById(R.id.cartLayout);
        emptyCartLayout = findViewById(R.id.emptyCartLayout);
        btnCheckout = findViewById(R.id.btnCheckout);


        //set layout manager
        cartRv.setLayoutManager(new LinearLayoutManager(this));

        //arraylist
        cartList = new ArrayList<>();
    }

    private double getFinalAmount(boolean isCouponCode) {
        return getTotalTaxAmount(isCouponCode) + getTotalPrice(isCouponCode);
    }

    private double getTotalTaxAmount(boolean isCouponCode) {
        double taxAmt = 0;
        for (int i = 0;i<cartList.size();i++) {
            ProductsModel model = cartList.get(i);
            if (isCouponCode && !model.isDiscount()) {
                //apply coupon on only undiscounted items
                double finalAmt = calculateDiscount(model.getProductActualPrice());
                taxAmt += calculateTax(finalAmt,model.getProductTax());
            }
            else {
                taxAmt += calculateTax(model.getProductActualPrice(),model.getProductTax());
            }
        }
        return taxAmt;
    }

    private double getTotalPrice(boolean isCouponCode) {
        double price = 0;
        for (int i = 0;i<cartList.size();i++) {
            ProductsModel model = cartList.get(i);

            if (isCouponCode && !model.isDiscount()) {
                //apply coupon on only undiscounted items
                double finalAmt = calculateDiscount(model.getProductActualPrice()); // 20% discount
                price += finalAmt;
            }
            else {
                price += model.getProductActualPrice();
            }
        }
        return price;
    }

    private double calculateDiscount(int price) {
        //calculate discount 20%
        double discount = (double) (price * 20) / 100;
        return price - discount ;
    }

    private double calculateTax(double amount, int taxPercent) {
        return (amount * taxPercent) / 100;
    }


}