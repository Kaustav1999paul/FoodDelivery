package Model;

public class OrderModel {

    private String  date, price, time, orderId, status;

    public OrderModel(){}

    public OrderModel(String date, String price, String time, String orderId, String status) {
        this.date = date;
        this.price = price;
        this.time = time;
        this.orderId = orderId;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


