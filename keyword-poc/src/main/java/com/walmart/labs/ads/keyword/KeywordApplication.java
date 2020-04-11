package com.walmart.labs.ads.keyword;

import com.walmart.labs.ads.keyword.db.mysql.MySQLBidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class KeywordApplication {
	private static final Logger log = LoggerFactory.getLogger(KeywordApplication.class);

	@Autowired
	MySQLBidService service;

	@PostConstruct
	private void init() {
		log.info("KeywordApplication initialization logic, build keyword lookup map ...");
		long start = System.currentTimeMillis();
		service.fullReadBlackedList();
		service.fullReadPlacementLevelBid();
		service.fullReadAdGroupKeywords();
		service.fullReadAdItemCampaign();
		service.fullReadItemPrimarySellers();
		log.info("Initializer " + (System.currentTimeMillis() - start) + " ms.");
	}

	public static void main(String[] args) {
		SpringApplication.run(KeywordApplication.class, args);
	}
}
