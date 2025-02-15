package com.paisa_square.paisa.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleSitemapPingService {

    public void pingGoogle() {
        String googlePingUrl = "https://www.google.com/ping?sitemap=https://paisquare.com/sitemap.xml";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(googlePingUrl, String.class);
    }
}

