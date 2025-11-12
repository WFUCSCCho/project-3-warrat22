/***********************************************
 * @file SP500.java
 * @description Class to represent data from the S&P 500 dataset.
The data consists of stock symbol and price
 * @author Alex Warren
 * @date November 12, 2025
 ***********************************************/

public class SP500 implements Comparable<SP500> {
    private String symbol;
    private double price;

    // Default constructor
    public SP500() {
        this.symbol = "";
        this.price = 0.0;
    }

    // Parameterized constructor
    public SP500(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    // Copy constructor
    public SP500(SP500 other) {
        this.symbol = other.symbol;
        this.price = other.price;
    }

    // print the SP500 object
    @Override
    public String toString() {
        return symbol + " " + price + " | ";
    }

    // determine if two objects are the same stock
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SP500)) {
            return false;
        }
        SP500 stock = (SP500) obj;
        return this.symbol.equals(stock.symbol);
    }

    @Override
    // compare two stocks by symbol
    public int compareTo(SP500 stock) {
        return this.symbol.compareTo(stock.symbol);
    }
}


