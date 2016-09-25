package com.anurag.pizzaboy;

import java.util.List;

/**
 * Created by Anurag on 9/24/2016.
 */

public class Pizza {
    private List<String> base;
    private List<String> toppings;

    Pizza(List<String> b, List<String> t) {
        base = b;
        toppings = t;
    }

    public List<String> getBase() {
        return base;
    }

    public List<String> getToppings() {
        return toppings;
    }
}
