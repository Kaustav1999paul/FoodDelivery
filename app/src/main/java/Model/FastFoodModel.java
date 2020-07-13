package Model;

public class FastFoodModel {

    private String Product_Image, category, date, foodName, foodid, price, time, cartId;

    public FastFoodModel(){}

    public FastFoodModel(String product_Image, String category, String date, String foodName, String foodid, String price, String time,String cartId) {
        Product_Image = product_Image;
        this.category = category;
        this.date = date;
        this.foodName = foodName;
        this.foodid = foodid;
        this.price = price;
        this.time = time;
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProduct_Image() {
        return Product_Image;
    }

    public void setProduct_Image(String product_Image) {
        Product_Image = product_Image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
