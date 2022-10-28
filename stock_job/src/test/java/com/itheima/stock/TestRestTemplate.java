package com.itheima.stock;

import com.itheima.stock.Service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author by itheima
 * @Date 2022/1/1
 * @Description
 */
@SpringBootTest
public class TestRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @Test
    public void test011(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    @Test
    public void test022(){
        stockTimerTaskService.getStockRtIndex();
    }

    @Test
    public void test033(){
        stockTimerTaskService.getStockPlate();
    }


}