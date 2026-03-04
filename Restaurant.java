package Hospital.demo.model;

class Restaurant {
    String foodItem;
    int price;
    int quantity;
    double finalBill;

    void takeOrder(String item, int cost, int qty) {
        foodItem = item;
        price = cost;
        quantity = qty;
        System.out.println("Order taken: " + quantity + " x " + foodItem + " @ ₹" + price);
    }

    void calculateBill() {
        double subtotal = price * quantity;
        double gst = subtotal * 0.05;
        finalBill = subtotal + gst;
        System.out.println("Final bill calculated with GST: ₹" + finalBill);
    }

    void showOrder() {
        System.out.println("Item: " + foodItem + ", Qty: " + quantity + ", Bill: ₹" + finalBill);
    }

    public static void main(String[] args) {
        Restaurant r = new Restaurant();
        r.takeOrder("Pizza", 200, 2);
        r.calculateBill();
        r.showOrder();
    }
}
