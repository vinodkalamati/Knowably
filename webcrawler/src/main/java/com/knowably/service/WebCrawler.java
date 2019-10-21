package com.knowably.service;

import com.knowably.model.Input;
import com.knowably.model.WebCrawl;

import java.util.List;

public interface WebCrawler {

    public List<WebCrawl> getContent(Input input);
}
