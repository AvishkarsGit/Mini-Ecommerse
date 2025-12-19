package com.avishkar.mini_e_commerse.constants;

import java.util.Random;

public class ProductConstants {
    public static final String clothes[] = {
                    "T-Shirt",
                    "Jeans",
                    "Jacket",
                    "Kurta",
                    "Hoodie",
                    "Shirt",
    };
    public static final String images[] = {
            "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab",
            "https://images.unsplash.com/photo-1542272604-787c3835535d",
            "https://images.unsplash.com/photo-1551028719-00167b16eac5",
            "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf",
            "https://images.unsplash.com/photo-1520975916090-3105956dac38",
            "https://images.unsplash.com/photo-1603252109303-2751441dd157",
    };

    public static int generatePrice() {
        Random random = new Random();
        return random.nextInt(1000);
    }

    public static int generateDiscountPrice() {
        Random random = new Random();
        return random.nextInt(1000) + 1000;
    }


}
