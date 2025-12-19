package com.avishkar.mini_e_commerse.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avishkar.mini_e_commerse.constants.DBConstant;
import com.avishkar.mini_e_commerse.models.ProductsModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, DBConstant.DB_NAME,null,DBConstant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table =
                "CREATE TABLE "+DBConstant.TABLE_NAME+
                        "("+
                        DBConstant.COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        DBConstant.COL_NAME+" TEXT,"+
                        DBConstant.COL_IMAGE+" TEXT,"+
                        DBConstant.COL_PRICE+" INTEGER,"+
                        DBConstant.COL_DISCOUNT_PRICE+" INTEGER DEFAULT 0,"+
                        DBConstant.COL_DISCOUNT+" INTEGER DEFAULT 0,"+
                        DBConstant.COL_CART+" INTEGER DEFAULT 0,"+
                        DBConstant.COL_TAX+" INTEGER DEFAULT 5"+
                        ");";

        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DBConstant.TABLE_NAME);
        onCreate(db);
    }

    public void addProduct(String name, String image, int price, int discount_price, boolean isDiscount, boolean isCart,int tax){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBConstant.COL_NAME,name);
        cv.put(DBConstant.COL_IMAGE,image);
        cv.put(DBConstant.COL_PRICE,price);
        cv.put(DBConstant.COL_DISCOUNT_PRICE,discount_price);
        cv.put(DBConstant.COL_DISCOUNT,(isDiscount) ? 1 : 0);
        cv.put(DBConstant.COL_CART,(isCart) ? 1 : 0);
        cv.put(DBConstant.COL_TAX,tax);
        database.insert(DBConstant.TABLE_NAME,null,cv);
    }

    public boolean isTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM "+DBConstant.TABLE_NAME+" LIMIT 1",null);
        boolean isEmpty = !cursor.moveToFirst();
        cursor.close();
        return isEmpty;
    }

    public ArrayList<ProductsModel> getAllProducts() {
        ArrayList<ProductsModel> productsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DBConstant.TABLE_NAME,null);
        while (cursor.moveToNext()) {
            ProductsModel model = new ProductsModel();
            model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_ID)));
            model.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DBConstant.COL_NAME)));
            model.setProductImg(cursor.getString(cursor.getColumnIndexOrThrow(DBConstant.COL_IMAGE)));
            model.setProductActualPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_PRICE)));
            model.setProductDiscountPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_DISCOUNT_PRICE)));
            int value = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_DISCOUNT));
            model.setDiscount(value==1);
            model.setProductTax(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_TAX)));
            int cart = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_CART));
            model.setCart(cart==1);
            productsList.add(model);
        }
        cursor.close();
        return productsList;
    }

    public Boolean addAndRemoveItemToCart(int id,boolean isAdded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBConstant.COL_CART,(isAdded) ? 1 : 0);
        int result = db.update(DBConstant.TABLE_NAME,cv,DBConstant.COL_ID+" = ?",new String[]{String.valueOf(id)});
        return result > 0;
    }

    public int getCartCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+DBConstant.TABLE_NAME+" WHERE "+DBConstant.COL_CART+" = 1",null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


    public ArrayList<ProductsModel> getCartItems() {
        ArrayList<ProductsModel> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DBConstant.TABLE_NAME+" WHERE "+DBConstant.COL_CART+" = 1",null);
        while (cursor.moveToNext()) {
            ProductsModel model = new ProductsModel();
            model.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_ID)));
            model.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DBConstant.COL_NAME)));
            model.setProductImg(cursor.getString(cursor.getColumnIndexOrThrow(DBConstant.COL_IMAGE)));
            model.setProductActualPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_PRICE)));
            model.setProductDiscountPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_DISCOUNT_PRICE)));
            int value = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_DISCOUNT));
            model.setDiscount(value==1);
            model.setProductTax(cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_TAX)));
            int cart = cursor.getInt(cursor.getColumnIndexOrThrow(DBConstant.COL_CART));
            model.setCart(cart==1);
            cartList.add(model);
        }
        cursor.close();
        return cartList;
    }

    public void removeItemsFromCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+DBConstant.TABLE_NAME+" SET "+DBConstant.COL_CART+" = 0 "+" WHERE "+DBConstant.COL_CART+" = 1";
        db.execSQL(query);
    }
}
