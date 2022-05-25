package be.pxl.auctions.rest.resource;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;

import java.time.LocalDate;
import java.util.Optional;

public class AuctionDTO {
    private long id;
    private String description;
    private LocalDate endDate;
    private boolean finished;
    private int numberOfBids;
    private double highestBid;
    private String highestBidBy;

    public AuctionDTO(){

    }

    public AuctionDTO(Auction auction){
        this.id = auction.getId();
        this.description = auction.getDescription();
        this.endDate = auction.getEndDate();
        this.finished = auction.isFinished();
        this.numberOfBids = auction.getBids().size();
        this.highestBid = highestBid(auction);
        this.highestBidBy = highestBidByUser(auction);
    }

    private double highestBid(Auction auction){
        Optional<Bid> bid =  auction.findHighestBid();
        return bid.map(Bid::getAmount).orElse(0.0);
    }

    private String highestBidByUser(Auction auction) {
        Optional<Bid> bid =  auction.findHighestBid();
        return bid.map(value -> value.getUser().getEmail()).orElse(null);
    }

    public long getId() { return id; }
    public String getDescription() { return description; }
    public LocalDate getEndDate() { return endDate; }
    public int getNumberOfBids() { return numberOfBids; }
    public double getHighestBid() { return highestBid; }
    public String getHighestBidBy() { return highestBidBy; }
    public boolean isFinished() { return finished; }


    public void setId(long id) { this.id = id; }
    public void setFinished(boolean finished) { this.finished = finished; }
    public void setDescription(String description) { this.description = description; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setNumberOfBids(int numberOfBids) { this.numberOfBids = numberOfBids; }
    public void setHighestBid(double highestBid) { this.highestBid = highestBid; }
    public void setHighestBidBy(String highestBidBy) { this.highestBidBy = highestBidBy; }
}
