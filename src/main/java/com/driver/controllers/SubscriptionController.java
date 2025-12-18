package com.driver.controllers;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subscription")
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;

    @PostMapping("/buy")
    public ResponseEntity buySubscription(@RequestBody SubscriptionEntryDto subscriptionEntryDto) {

        //We need to buy subscription and save its relevant subscription to the db and return the finalAmount
        try {
            int finalAmount = subscriptionService.buySubscription(subscriptionEntryDto);
            return new ResponseEntity<>(finalAmount, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upgradeSubscription/{userId}")
    public ResponseEntity upgradeSubscription(@PathVariable("userId") Integer userId) {

        //In this function you need to upgrade the subscription to  its next level
        //ie if You are A BASIC subscriber update to PRO and if You are a PRO upgrade to ELITE.
        //Incase you are already an ELITE member throw an Exception
        //and at the end return the difference in fare that you need to pay to get this subscription done.
        try {
            int newAmount =  subscriptionService.upgradeSubscription(userId);
            return new ResponseEntity(newAmount, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/calculateTotalRevenue")
    public Integer getTotalRevenue() {

        //Calculate the total Revenue of hot-star from all the Users combined...
        return subscriptionService.calculateTotalRevenueOfHotstar();

    }
}