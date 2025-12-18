package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.Exception.UserNotFoundException;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    // make it global so that i can use this in whole code of service class
    Subscription subscription = new Subscription();

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay

        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        if(!optionalUser.isPresent()){
            throw new UserNotFoundException("invalid user_id: " + subscriptionEntryDto.getUserId());
        }
        subscription.setUser(optionalUser.get());
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        // calculate amount to pay (subscription plan)
        int totalAmount = 0;
        if(subscriptionEntryDto.getSubscriptionType() == SubscriptionType.ELITE){
            totalAmount += 1000;
        }
        else if(subscriptionEntryDto.getSubscriptionType() == SubscriptionType.PRO){
            totalAmount += 800;
        }
        else if(subscriptionEntryDto.getSubscriptionType() == SubscriptionType.BASIC){  // for basic plan
            totalAmount += 500;
        }
        subscription.setTotalAmountPaid(totalAmount);

        subscriptionRepository.save(subscription);
        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        SubscriptionType subscriptionType = user.getSubscription().getSubscriptionType();
        int newAmount = 0;
        if(subscriptionType ==SubscriptionType.ELITE){
            throw new Exception("Already the best Subscription");
        }
        else if(subscriptionType ==SubscriptionType.PRO){
            subscription.setSubscriptionType(SubscriptionType.ELITE);  // Upgrade the plan
            newAmount += 1000-800;
        }
        else if (subscriptionType == SubscriptionType.BASIC){
            // upgrade to PRO plan
            subscription.setSubscriptionType(SubscriptionType.PRO);
            newAmount += 800-500;
        }
        subscriptionRepository.save(subscription);
        return newAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptions = subscriptionRepository.findAll();
        int totalRevenue = 0;
        for(Subscription subscription1:subscriptions){
            totalRevenue += subscription1.getTotalAmountPaid();
        }

        return totalRevenue;
    }

}
