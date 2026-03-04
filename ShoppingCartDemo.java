package ecom.demo.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ShoppingCartDemo {

    static final class Product {
        private final String productId;    
        private final String name;
        private final BigDecimal unitPrice;

        public Product(String productId, String name, BigDecimal unitPrice) {
            this.productId = Objects.requireNonNull(productId);
            this.name = Objects.requireNonNull(name);
            this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
        }

        public String getProductId() { return productId; }
        public String getName() { return name; }
        public BigDecimal getUnitPrice() { return unitPrice; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Product)) return false;
            Product product = (Product) o;
            return productId.equals(product.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId);
        }

        @Override
        public String toString() {
            return String.format("%s (%s) ₹%s", name, productId, unitPrice);
        }
    }

  
    static final class CartItem {
        private final Product product;
        private int quantity;
        private final BigDecimal unitPriceAtAdd;

        public CartItem(Product product, int quantity) {
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be >= 1");
            this.product = Objects.requireNonNull(product);
            this.quantity = quantity;
            this.unitPriceAtAdd = product.getUnitPrice();
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) {
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be >= 1");
            this.quantity = quantity;
        }

        public BigDecimal getLineTotal() {
            return unitPriceAtAdd.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
        }

        @Override
        public String toString() {
            return String.format("%-20s x %d @ ₹%s = ₹%s",
                    product.getName(), quantity, unitPriceAtAdd, getLineTotal());
        }
    }

    static final class ShoppingCart {
       
        private final List<CartItem> lineItems = new ArrayList<>();
      
        private final Map<Product, Integer> quantityByProduct = new HashMap<>();

        private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); 

        /**
         * Add product to cart. If it exists, increases quantity.
         */
        public void addProduct(Product p, int qty) {
            if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
            Integer existing = quantityByProduct.get(p);
            if (existing == null) {
                
                lineItems.add(new CartItem(p, qty));
                quantityByProduct.put(p, qty);
            } else {
               
                int newQty = existing + qty;
                quantityByProduct.put(p, newQty);
               
                getCartItem(p).ifPresent(item -> item.setQuantity(newQty));
            }
        }

      
        public void setQuantity(Product p, int qty) {
            if (qty <= 0) {
                removeProduct(p);
                return;
            }
            if (!quantityByProduct.containsKey(p)) {
              
                addProduct(p, qty);
                return;
            }
            quantityByProduct.put(p, qty);
            getCartItem(p).ifPresent(item -> item.setQuantity(qty));
        }

        public void increaseQuantity(Product p, int delta) {
            if (delta <= 0) throw new IllegalArgumentException("Delta must be > 0");
            if (!quantityByProduct.containsKey(p)) {
                addProduct(p, delta);
                return;
            }
            int newQty = quantityByProduct.get(p) + delta;
            quantityByProduct.put(p, newQty);
            getCartItem(p).ifPresent(item -> item.setQuantity(newQty));
        }

       
        public void decreaseQuantity(Product p, int delta) {
            if (delta <= 0) throw new IllegalArgumentException("Delta must be > 0");
            Integer current = quantityByProduct.get(p);
            if (current == null) return; 
            int newQty = current - delta;
            if (newQty <= 0) {
                removeProduct(p);
            } else {
                quantityByProduct.put(p, newQty);
                getCartItem(p).ifPresent(item -> item.setQuantity(newQty));
            }
        }

        
        public void removeProduct(Product p) {
            quantityByProduct.remove(p);
            lineItems.removeIf(item -> item.getProduct().equals(p));
        }

        
        public void clear() {
            quantityByProduct.clear();
            lineItems.clear();
        }

        public BigDecimal getSubTotal() {
            BigDecimal sum = BigDecimal.ZERO;
            for (CartItem item : lineItems) {
                sum = sum.add(item.getLineTotal());
            }
            return sum.setScale(2, RoundingMode.HALF_UP);
        }

        public BigDecimal getTax() {
            return getSubTotal().multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        }

        
        public BigDecimal getGrandTotal() {
            return getSubTotal().add(getTax()).setScale(2, RoundingMode.HALF_UP);
        }

        public List<CartItem> getLineItems() {
            return Collections.unmodifiableList(lineItems);
        }

        
        public int getQuantity(Product p) {
            return quantityByProduct.getOrDefault(p, 0);
        }

        private Optional<CartItem> getCartItem(Product p) {
            for (CartItem item : lineItems) {
                if (item.getProduct().equals(p)) return Optional.of(item);
            }
            return Optional.empty();
        }

        
        public void printReceipt() {
            System.out.println("\n--- Cart Summary ---");
            if (lineItems.isEmpty()) {
                System.out.println("(empty cart)");
                return;
            }
            for (CartItem item : lineItems) {
                System.out.println(item);
            }
            System.out.println("--------------------");
            System.out.println(String.format("Subtotal : ₹%s", getSubTotal()));
            System.out.println(String.format("Tax (10%%): ₹%s", getTax()));
            System.out.println(String.format("Total    : ₹%s", getGrandTotal()));
        }
    }

   
    public static void main(String[] args) {
       
        Product p1 = new Product("P1001", "Wireless Mouse", new BigDecimal("599.00"));
        Product p2 = new Product("P2001", "Mechanical Keyboard", new BigDecimal("2499.50"));
        Product p3 = new Product("P3001", "USB-C Hub", new BigDecimal("1099.99"));

        ShoppingCart cart = new ShoppingCart();

 
        cart.addProduct(p1, 1);
        cart.addProduct(p2, 2);
        cart.addProduct(p3, 1);

        cart.printReceipt();

       
        cart.increaseQuantity(p1, 2); 
        cart.decreaseQuantity(p2, 1); 
        cart.setQuantity(p3, 4);      
        cart.printReceipt();

        cart.removeProduct(p2); 

        cart.printReceipt();

        
        System.out.println("\n(Quick quantities via HashMap)");
        System.out.println(p1.getName() + " qty = " + cart.getQuantity(p1));
        System.out.println(p2.getName() + " qty = " + cart.getQuantity(p2));
        System.out.println(p3.getName() + " qty = " + cart.getQuantity(p3));
    }
}
