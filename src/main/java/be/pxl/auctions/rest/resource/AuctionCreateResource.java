package be.pxl.auctions.rest.resource;

import be.pxl.auctions.model.Bid;

import java.util.ArrayList;
import java.util.List;

public class AuctionCreateResource {
    private String description;
    private String endDate;
    private List<Bid> bidList = new ArrayList<>();

    public String getDescription() { return description; }
    public String getEndDate() { return endDate; }
    public List<Bid> getBidList() {
        return bidList;
    }

    public void setDescription(String description) { this.description = description; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
