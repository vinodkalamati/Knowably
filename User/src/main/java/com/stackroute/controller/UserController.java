package com.stackroute.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.domain.User;
import com.stackroute.exception.UserAlreadyExistException;
import com.stackroute.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class UserController {
    UserServices userServices;
    private String topic="TopicTest";

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public UserController(UserServices userServices){
        this.userServices=userServices;
    }
    //kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic WriteYourTopicName --from-beginning
    @PostMapping("user/{message}")
    public ResponseEntity<?> saveUser(@RequestBody User user,@PathVariable("message") String message){
        ResponseEntity responseEntity;
        try{
            userServices.saveUser(user);
            responseEntity=new ResponseEntity<String>("successfully created", HttpStatus.CREATED);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(user);
                System.out.println("Resulting JSON string = " + json);
                kafkaTemplate.send(topic,json);
                //kafkaTemplate.send(topic,1,"msg=",message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        catch (UserAlreadyExistException e){
            responseEntity=new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }

    @GetMapping("user")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<List<User>>(userServices.getAllUsers(),HttpStatus.OK);
    }
}
