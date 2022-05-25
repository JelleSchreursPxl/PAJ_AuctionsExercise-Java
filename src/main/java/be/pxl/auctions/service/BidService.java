package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionRepository;
import be.pxl.auctions.dao.BidRepository;
import be.pxl.auctions.dao.UserRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.util.EmailValidator;
import be.pxl.auctions.util.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    @Autowired
    public BidService(BidRepository bidRepository, UserRepository userRepository, AuctionRepository auctionRepository){
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
    }

    public BidDTO createBid(BidCreateResource bidInfo, Long auctionId) throws RequiredFieldException, InvalidEmailException, DuplicateEmailException {
        Optional<User> optionalUser = userRepository.findUserByEmail(bidInfo.getEmail());
        Optional<Auction> optionalAuction = auctionRepository.findAuctionById(auctionId);
        if(optionalAuction.isPresent()){
            if(optionalUser.isPresent()){
                if(userRepository.existsById(optionalUser.get().getId())){
                    Bid bid = mapToBid(bidInfo);
                    return mapToBidResource(bidRepository.save(bid));
                }
            }
        }
        throw new AuctionNotFoundException("No auction has been found");
    }

    private BidDTO mapToBidResource(Bid bid){
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setPrice(bid.getAmount());
        bidDTO.setDate(LocalDate.now());
        return bidDTO;
    }

    private Bid mapToBid(BidCreateResource bidCreateResource){
        Bid bid = new Bid();
        bid.setAmount(Double.parseDouble(bidCreateResource.getPrice()));
        bid.setUser(bidCreateResource.getUser());
        bid.setAuction(bidCreateResource.getAuction());
        bid.setDate(LocalDate.now());
        return bid;
    }
}
