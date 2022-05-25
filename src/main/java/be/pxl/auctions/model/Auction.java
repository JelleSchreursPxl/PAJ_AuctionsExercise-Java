package be.pxl.auctions.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private LocalDate endDate;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    public List<Bid> bids = new ArrayList<>();


    public Auction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public boolean isFinished() {
        return getEndDate().isBefore(LocalDate.now());
    }

    public Optional<Bid> findHighestBid(){
        if(bids.size() == 0){
            return Optional.empty();
        }
        return bids.stream().max(Comparator.comparing(Bid::getAmount));
    }
}
