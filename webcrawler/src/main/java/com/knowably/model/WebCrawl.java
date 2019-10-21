package com.knowably.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebCrawl {

    private String id;
    private String userId;
    private String domain;
    private String concept;
    private String url;
    private List<String> payload;
}
