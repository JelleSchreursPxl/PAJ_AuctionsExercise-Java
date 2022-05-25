package be.pxl.auctions.rest.resource;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.User;

public class BidCreateResource {
    private String email;
    private String price;
    private Auction auction;
    private User user;

    public String getEmail() { return email; }
    public String getPrice() { return price; }
    public Auction getAuction() { return auction; }
    public User getUser() { return user; }

    public void setEmail(String email) { this.email = email; }
    public void setPrice(String price) { this.price = price; }
    public void setAuction(Auction auction) { this.auction = auction; }
    public void setUser(User user) { this.user = user; }
}
