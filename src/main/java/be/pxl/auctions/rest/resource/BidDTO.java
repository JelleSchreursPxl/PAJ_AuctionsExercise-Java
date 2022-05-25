package be.pxl.auctions.rest.resource;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.User;

import java.time.LocalDate;

public class BidDTO {
    private long id;
    private double price;
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
