package com.knowably.service;

import com.knowably.model.Input;
import com.knowably.model.WebCrawl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebCrawlerImpl implements WebCrawler {
    private List<String> content;
    private List<WebCrawl> results;
    private int count=0;

    public WebCrawlerImpl() {
    }

@Override
    public List<WebCrawl> getContent(Input input) {
        String[] URLs=input.getUrl();
        count=0;
    results=new ArrayList<>();
    while(URLs.length>count){
        String URL=URLs[count];
            System.out.println(" [" + URL + "]");
            try {
                Document document = Jsoup.connect(URL).get();
                //change Css query to get required content
                Elements body = document.select("body");
                content = new ArrayList<>();
                for (Element element : body) {
                    System.out.println(element);
                    content.add(element.toString());
                }
                count++;
                System.out.println(content.size());
                WebCrawl webCrawl =new WebCrawl();
                webCrawl.setId(input.getId());
                webCrawl.setConcept(input.getConcept());
                webCrawl.setDomain(input.getDomain());
                webCrawl.setUrl(URL);
                webCrawl.setPayload(content);
                results.add(webCrawl);
//                System.out.println(results.toString());
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }


        return results;
    }


}



/*    public void getPageTitle(String URL) {
        if ((!links.contains(URL))) {
            System.out.println(" [" + URL + "]");
            try {
                links.add(URL);
                Document document = Jsoup.connect(URL).get();
                Elements titles = document.select("title");

                for (Element title : titles) {
                    System.out.println(title);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }*/
