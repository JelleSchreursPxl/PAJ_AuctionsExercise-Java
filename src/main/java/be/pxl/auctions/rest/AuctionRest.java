package be.pxl.auctions.rest;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.rest.resource.*;
import be.pxl.auctions.service.AuctionService;
import be.pxl.auctions.service.BidService;
import be.pxl.auctions.util.exception.AuctionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest")
public class AuctionRest {
    private static final Logger LOGGER = LogManager.getLogger(AuctionRest.class);

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private BidService bidService;


    @GetMapping("auctions")
    public List<AuctionDTO> getAllAuctions(){
        return auctionService.getAllAuctions().stream().filter(a -> !a.isFinished()).collect(Collectors.toList());
    }

    @GetMapping("auctions/{auctionId}")
    public AuctionDTO getAuctionById(@PathVariable("auctionId") long auctionId){
        return auctionService.getAuctionById(auctionId);
    }

    @PostMapping("auctions")
    public AuctionDTO createAuction(@RequestBody AuctionCreateResource auctionCreateResource){
        return auctionService.createAuction(auctionCreateResource);
    }

    @PostMapping("auctions/{auctionId}/bids")
    public BidDTO createBid(@RequestBody BidCreateResource bidCreateResource, @PathVariable("auctionId") long auctionId){
        AuctionDTO auctionDTO = auctionService.getAuctionById(auctionId);
        if(!auctionDTO.isFinished()){
            throw new AuctionNotFoundException("Auction {" + auctionDTO.getDescription() + "} is finished");
        }
        if(Objects.equals(auctionDTO.getHighestBidBy(), bidCreateResource.getEmail())){
            throw new AuctionNotFoundException("You're already winning this auction");
        }
        if(auctionDTO.getHighestBid() > Double.parseDouble(bidCreateResource.getPrice())){
            throw new AuctionNotFoundException("Your bid must exceed the previous highest bid of :" + auctionDTO.getHighestBid());
        }
        return bidService.createBid(bidCreateResource, auctionId);
    }

}
