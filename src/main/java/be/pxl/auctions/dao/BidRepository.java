package be.pxl.auctions.dao;


import be.pxl.auctions.model.Bid;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends CrudRepository<Bid, Long> {
    Optional<Bid> findBidById(long bidId);
    List<Bid> findAll();
}
