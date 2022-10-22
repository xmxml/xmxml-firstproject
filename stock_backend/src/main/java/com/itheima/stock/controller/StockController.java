package com.itheima.stock.controller;


import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> findStockMarket(){
        return stockService.findStockMarket();
    }

    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> findStockBlock(){
        return stockService.findStockBlock();
    }
}