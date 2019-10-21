package com.knowably.service;

import com.knowably.domain.Input;
import com.knowably.domain.WebCrawl;

import java.util.List;

public interface WebCrawler {

    public List<WebCrawl> getContent(Input input);

    /*@KafkaListener(topics = "TopicTest",groupId = "group_id")
    void consume(String msg);*/
}
