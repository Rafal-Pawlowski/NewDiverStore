//package com.ralf.NewDiverStore.currency.service;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Map;
//
//@Service
//public class CurrencyExchangeService {
//
//    @Value("${fixer.api.url}")
//    private String apiUrl;
//
//    @Value("${fixer.api.key}")
//    private String apiKey;
//
//
//    private final RestTemplate restTemplate;
//
//    public CurrencyExchangeService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//    public BigDecimal getExchangeRate(String targetCurrency) {
//        // Endpoint Fixer.io
//        String url = apiUrl + "/latest?access_key=" + apiKey + "&symbols=" + targetCurrency;
//
//        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//        if (response != null && response.containsKey("rates")) {
//            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
//            Object rateObj = rates.get(targetCurrency.toUpperCase());
//
//            if (rateObj instanceof Integer) {
//                return BigDecimal.valueOf((Integer) rateObj).setScale(2, RoundingMode.HALF_UP);
//            } else if (rateObj instanceof Double) {
//                return BigDecimal.valueOf((Double) rateObj).setScale(2, RoundingMode.HALF_UP);
//            }
//        }
//
//        throw new RuntimeException("Failed to fetch exchange rate for currency: " + targetCurrency);
//    }
//}
