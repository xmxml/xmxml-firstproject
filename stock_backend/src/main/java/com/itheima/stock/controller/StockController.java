package com.itheima.stock.controller;


import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.pojo.vo.PageResult;
import com.itheima.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quot")
@Api(value = "用户查询股票信息",tags = "股票查询")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/index/all")
    @ApiOperation(value = "A股国内大盘",response = R.class)
    public R<List<InnerMarketDomain>> findStockMarket(){
        return stockService.findStockMarket();
    }

    @GetMapping("/sector/all")
    @ApiOperation(value = "国内板块数据分析",response = R.class)
    public R<List<StockBlockDomain>> findStockBlock(){
        return stockService.findStockBlock();
    }


    @GetMapping("/stock/all")
    @ApiOperation(value = "涨幅榜的分页查询",response = R.class)
    public R<PageResult<StockUpdownDomain>> findByPageStock(
            @RequestParam(name="Page",required = false,defaultValue = "1") Integer Page,
            @RequestParam(name="PageSize",required = false,defaultValue = "20")Integer PageSize
    ){
        return stockService.findByPageStock(Page,PageSize);
    }

    @GetMapping("/stock/increase")
    @ApiOperation(value = "涨幅榜的前五名",response = R.class)
    public R<List<StockUpdownDomain>> findStockGain(){
        return stockService.findStockGain();
    }

    @GetMapping("/stock/updown/count")
    @ApiOperation(value = "涨停跌停指数",response = R.class)
    public R<Map>  findStockUpdownCount(){
        return stockService.findStockUpdownCount();
    }
}
