package com.suusoft.restaurantuser.cart;

import android.content.Context;

import com.suusoft.restaurantuser.model.Food;
import com.suusoft.restaurantuser.model.ProductOption;
import com.suusoft.restaurantuser.model.ProductVersion;

import java.util.ArrayList;

/**
 * Created by Suusoft on 27/07/2017.
 */

public class CartUtils extends CartManager {
    private double subTotal = 0;
    public double deliveryFee = 5.00;
    public ArrayList<Integer> listRiderTips;

    public CartUtils(Context context) {
        super(context);
        listRiderTips = new ArrayList<>();
    }


    public ArrayList<Food> getCart() {
        return super.getCart();
    }

    public double getTotal() {
        try {
            double total = 0;
            ArrayList<Food> listFoods = getCart();
            for (Food item : listFoods) {
                ArrayList<ProductVersion> versions = item.getProduct_version();
                if (versions == null || versions.isEmpty()) {
                    if (item.isSale()) {
                        double priceItem = Double.valueOf(item.getPrice());
                        priceItem = priceItem - (item.getDiscountPromoCode() * priceItem / 100);
                        total += item.getQuantity() * priceItem;
                    } else {
                        total += item.getQuantity() * Double.valueOf(item.getPrice());
                    }
                } else {
                    for (ProductVersion version : versions) {
                        if (version.isSelected()) {
                            ArrayList<ProductOption> options = version.getProduct_option();
                            if (options == null || options.isEmpty()) {
                                if (item.isSale()) {
                                    double priceItem = Double.valueOf(version.getPrice());
                                    priceItem = priceItem - (item.getDiscountPromoCode() * priceItem / 100);
                                    total += item.getQuantity() * priceItem;
                                } else {
                                    total += item.getQuantity() * Double.valueOf(version.getPrice());
                                }

                            } else {
                                double priceTopping = 0;
                                for (ProductOption option : options) {
                                    if (option.isSelected()) {
                                        priceTopping = priceTopping + Double.parseDouble(option.getPrice());
                                    }
                                }

                                if (item.isSale()) {
                                    double priceItem = Double.valueOf(version.getPrice());
                                    priceItem = priceItem - (item.getDiscountPromoCode() * priceItem / 100);
                                    total += item.getQuantity() * (priceItem + priceTopping);
                                } else {
                                    total += item.getQuantity() * (Double.valueOf(version.getPrice()) + priceTopping);
                                }

                            }

                        }
                    }
                }
            }
            subTotal = ((double) Math.round(total * 100) / 100);
            return subTotal;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public double getTotalDeliveryFee() {
        return subTotal + deliveryFee;
    }

    @Override
    public double getDeliveryFee() {
        return deliveryFee;
    }

    @Override
    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @Override
    public ArrayList<Integer> getRiderTips() {
        return listRiderTips;
    }

    @Override
    public <T> double calculatePriceItem(T obj) {
        Food model = (Food) obj;
        if (!model.isHasVersion()) {
            double priceTotal = Double.parseDouble(model.getPrice());
            model.setPrice_total(priceTotal * model.getQuantity());
            return priceTotal;
        } else {
            ArrayList<ProductVersion> versions = model.getProduct_version();
            for (ProductVersion version : versions) {
                if (version.isSelected()) {
                    if (!version.isHasOption()) {
                        double priceTotal = Double.parseDouble(version.getPrice());
                        model.setPrice_total(priceTotal * model.getQuantity());
                        return priceTotal;
                    } else {
                        ArrayList<ProductOption> options = version.getProduct_option();
                        double total = Double.parseDouble(version.getPrice());
                        for (ProductOption option : options) {
                            if (option.isSelected()) {
                                total += Double.parseDouble(option.getPrice());
                            }
                        }
                        model.setPrice_total(total * model.getQuantity());
                        return total;
                    }
                }
            }
            return 0;
        }
    }

    public <T> double calculatePriceItemSale(T obj) {
        Food model = (Food) obj;
        if (!model.isHasVersion()) {
            double priceItem = Double.valueOf(model.getPrice());
            model.setPrice_total(priceItem * model.getQuantity());
            priceItem = priceItem - (model.getDiscountPromoCode() * priceItem / 100);
            model.setPrice_discount(priceItem * model.getQuantity());
            return priceItem;
        } else {
            ArrayList<ProductVersion> versions = model.getProduct_version();
            for (ProductVersion version : versions) {
                if (version.isSelected()) {
                    if (!version.isHasOption()) {
                        double priceItem = Double.parseDouble(version.getPrice());
                        model.setPrice_total(priceItem * model.getQuantity());
                        priceItem = priceItem - (model.getDiscountPromoCode() * priceItem / 100);
                        model.setPrice_discount(priceItem * model.getQuantity());
                        return priceItem;
                    } else {
                        ArrayList<ProductOption> options = version.getProduct_option();
                        double priceItem = Double.parseDouble(version.getPrice());
                        double priceTotal = Double.parseDouble(version.getPrice());
                        priceItem = priceItem - (model.getDiscountPromoCode() * priceItem / 100);

                        for (ProductOption option : options) {
                            if (option.isSelected()) {
                                priceItem += Double.parseDouble(option.getPrice());
                                priceTotal += Double.parseDouble(option.getPrice());
                            }
                        }
                        model.setPrice_total(priceTotal * model.getQuantity());
                        model.setPrice_discount(priceItem * model.getQuantity());
                        return priceItem;
                    }
                }
            }
            return 0;
        }
    }

    @Override
    public <T> void addToCart(T o) {
        Food model = (Food) o;
        ArrayList<Food> listFoods = getCart();
        try {
            boolean isExist = false;
            boolean isOther = false;
            outer:

            for (int i = 0; i < super.countItems(); i++) {
                Food old = listFoods.get(i);
                item_other:
                if (old.getId().equals(model.getId())) {
                    isExist = true;
                    if (!model.isHasVersion()) {
                        old.setQuantity((old.getQuantity() + model.getQuantity()));
                        break;
                    } else {
                        ArrayList<ProductVersion> versionsNew = model.getProduct_version();
                        ArrayList<ProductVersion> versionsOld = old.getProduct_version();
                        for (ProductVersion newPV : versionsNew) {
                            if (newPV.isSelected()) {
                                ArrayList<ProductOption> new_options = newPV.getProduct_option();
                                for (ProductVersion oldPV : versionsOld) {
                                    if (newPV.getName().equals(oldPV.getName())) {
                                        if (oldPV.isSelected()) {
                                            ArrayList<ProductOption> old_options = oldPV.getProduct_option();
                                            if (!newPV.isHasOption()) {
                                                old.setQuantity((old.getQuantity() + model.getQuantity()));
                                                break outer;
                                            } else {
                                                for (int n = 0, j = 0; n < old_options.size(); n++, j++) {
                                                    ProductOption newOption = new_options.get(n);
                                                    ProductOption oldOption = old_options.get(j);
                                                    if (!newOption.isSelected() && oldOption.isSelected() || newOption.isSelected() && !oldOption.isSelected()) {
                                                        isOther = true;
                                                        break item_other;
                                                    } else {
                                                        isOther = false;
                                                    }
                                                }
                                                if (!isOther) {
                                                    old.setQuantity((old.getQuantity() + model.getQuantity()));
                                                    break outer;
                                                }
                                            }
                                        } else {
                                            isOther = true;
                                        }
                                    }

                                }
                            }

                        }
                    }

                }
            }

            if (!isExist || isOther) {
                getCart().add(model);
            }
            notifiCartChange();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public int countItems() {
        int count = 0;
        ArrayList<Food> listFoods = getCart();
        for (int i = 0; i < super.countItems(); i++) {
            Food food = listFoods.get(i);
            count += food.getQuantity();
        }
        return count;
    }


}
