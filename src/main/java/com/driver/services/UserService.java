package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User user1 = userRepository.save(user);
        return user1.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        // find subscription of the given user_id:
       Optional<Subscription> userSubscription = subscriptionRepository.findById(userId);
       if(!userSubscription.isPresent()){
          return 0; // this user_id has no subscription
       }
        Subscription UserSubscription =  userSubscription.get();

       // find age of this user
       int userAge = UserSubscription.getUser().getAge();

       // find subscription type of this user
      SubscriptionType userSubscriptionType = UserSubscription.getSubscriptionType();

     // get all webseries
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        int webseriesCount = 0;
//        for(WebSeries webSeries:webSeriesList){
//            if(userAge>=webSeries.getAgeLimit()){
//                // then check subscription type
//                if(userSubscriptionType.equals(webSeries.getSubscriptionType())){
//                    webseriesCount++;
//                }
//            }
//        }
        // Filter web series based on age and subscription type
        long count = webSeriesList.stream()
                .filter(ws -> userAge >= ws.getAgeLimit()) // age check
                .filter(ws -> ws.getSubscriptionType().equals(userSubscriptionType)) // subscription check
                .count();


        return (int)count;
    }


}
