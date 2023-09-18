package template.shop.data;

import template.shop.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {
    private static List<ItemModel> cart = new ArrayList<>();

    public static void addCart(ItemModel model) {
        cart.add(model);
    }
    public static void removeCart(ItemModel model) {
        for (int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getId()==model.getId()){
                cart.remove(i);
                break;
            }
        }
    }
    public static void clearCart() {
        cart.clear();
    }
    public static List<ItemModel> getCart() {
        return cart;
    }
    public static long getCartPriceTotal() {
        long total = 0;
        for (int i = 0; i < cart.size(); i++) {
            total = total + cart.get(i).getSumPrice();
        }
        return total;
    }
    public static int getCartItemTotal() {
        int total = 0;
        for (int i = 0; i < cart.size(); i++) {
            total = total + cart.get(i).getTotal();
        }
        return total;
    }
    public static int getCartItem() {
        return cart.size();
    }
    public static void updateItemTotal(ItemModel model) {
        for (int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getId()==model.getId()){
                cart.remove(i);
                cart.add(i, model);
                break;
            }
        }
    }

    public static boolean isCartExist(ItemModel model){
        for (int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getId()==model.getId()){
                return true;
            }
        }
        return false;
    }
}
