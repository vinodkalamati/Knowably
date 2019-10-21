package com.knowably.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.knowably.domain.Input;
import com.knowably.domain.WebCrawl;
import com.knowably.service.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "api/v1")
public class WebCrawlerController {

    private WebCrawler webCrawler;
    private Input input;
    private ResponseEntity responseEntity;
    private String topic="TopicPayload";

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;


    @Autowired
    public WebCrawlerController(WebCrawler webCrawler) {
        this.webCrawler = webCrawler;
    }


    @GetMapping("content")
    public ResponseEntity<?> getContent(){
        try{
            List<WebCrawl> lists=webCrawler.getContent(input);
            responseEntity = new ResponseEntity<List<WebCrawl>>(lists, HttpStatus.OK);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String listPayload = mapper.writeValueAsString(lists);
                //System.out.println("Resulting JSON string = " + json);
                kafkaTemplate.send(topic,listPayload);
                //kafkaTemplate.send(topic,1,"msg=",message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            responseEntity = new ResponseEntity<String>("No Content Found",HttpStatus.NOT_FOUND);

        }
        return responseEntity;
    }

    @PostMapping("content")
    public ResponseEntity<String> domainUpload(@RequestBody Input input1)
    {
        try {
            input = new Input();
            input.setDomain(input1.getDomain());
            String concept = input1.getConcept();
            input.setConcept(concept);
            input.setUserId(input1.getUserId());
            input.setUrl(input1.getUrl());
            input.setId(input1.getId());
            responseEntity = new ResponseEntity<String>("Uploaded Succesfully",HttpStatus.OK);
        } catch (Exception e)
        {
            responseEntity = new ResponseEntity<String>("Error in Uploading",HttpStatus.CONFLICT);
        }
        return responseEntity;
    }


    @KafkaListener(topics = "TopicLinks",groupId = "services")
    public void consume(String receivedInput) {

        System.out.println("ndjnfjdnfjd");
        System.out.println(receivedInput);
        Gson gson=new Gson();
        Input input1=gson.fromJson(receivedInput,Input.class);
        input = new Input();
        input.setDomain(input1.getDomain());
        String concept = input1.getConcept();
        input.setConcept(concept);
        input.setUserId(input1.getUserId());
        input.setUrl(input1.getUrl());
        input.setId(input1.getId());
        List<WebCrawl> webCrawls=webCrawler.getContent(input);

        ObjectMapper objectMapper=new ObjectMapper();

        try {
            String resultPayload= objectMapper.writeValueAsString(webCrawls);
            kafkaTemplate.send(topic,resultPayload);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
