package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.util.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository){
        this.auctionRepository = auctionRepository;
    }

    public List<AuctionDTO> getAllAuctions(){
        return auctionRepository.findAll().stream().map(this::mapToAuctionResource).collect(Collectors.toList());
    }

    public AuctionDTO getAuctionById(long auctionId){
        return auctionRepository.findAuctionById(auctionId).map(this::mapToAuctionResource).orElseThrow(() -> new AuctionNotFoundException("Unable to find Auction with id [" + auctionId + "]"));
    }

    public AuctionDTO createAuction(AuctionCreateResource auctionInfo) throws RequiredFieldException, InvalidDateException {
        if(StringUtils.isBlank(auctionInfo.getDescription())) {
            throw new RequiredFieldException("Description");
        }
        if(StringUtils.isBlank(auctionInfo.getEndDate()) || auctionInfo.getEndDate() == null){
            throw new RequiredFieldException("EndDate");
        }

        Auction auction = mapToAuction(auctionInfo);
        if(auction.getEndDate().isBefore(LocalDate.now())){
            throw new AuctionNotFoundException("Date can't be in the past");
        }
        return mapToAuctionResource(auctionRepository.save(auction));
    }

    private AuctionDTO mapToAuctionResource(Auction auction){
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(auction.getId());
        auctionDTO.setDescription(auction.getDescription());
        auctionDTO.setEndDate(auction.getEndDate());
        auctionDTO.setFinished(auction.isFinished());
        auctionDTO.setNumberOfBids(auction.getBids().size());

        Optional<Bid> bid = auction.findHighestBid();
        if (bid.isPresent()){
            auctionDTO.setHighestBid(bid.get().getAmount());
            auctionDTO.setHighestBidBy(bid.get().getUser().getEmail());
        }

        return auctionDTO;
    }

    private Auction mapToAuction(AuctionCreateResource auctionCreateResource) throws InvalidDateException {
        Auction auction = new Auction();
        auction.setDescription(auctionCreateResource.getDescription());
        try{
            auction.setEndDate(LocalDate.parse(auctionCreateResource.getEndDate(), DATE_FORMAT));
        }catch (DateTimeParseException e) {
            throw new InvalidDateException("[" + auction.getEndDate() + "] is not a valid date. Excepted format: dd/mm/yyyy");
        }
        auction.setBids(auctionCreateResource.getBidList());
        return auction;

    }
}
