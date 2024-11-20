//package com.ralf.NewDiverStore.currency.controller;
//
//import com.ralf.NewDiverStore.currency.service.CurrencyExchangeService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigDecimal;
//
//@RestController
//public class CurrencyApiController {
//
//private final CurrencyExchangeService currencyExchangeService;
//
//    public CurrencyApiController(CurrencyExchangeService currencyExchangeService) {
//        this.currencyExchangeService = currencyExchangeService;
//    }
//
//    @GetMapping("/exchange-rate")
//    public BigDecimal getExchangeRate(@RequestParam String currency) {
//        return currencyExchangeService.getExchangeRate(currency);
//    }
//}
