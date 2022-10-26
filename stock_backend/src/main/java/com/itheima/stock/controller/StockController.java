package com.itheima.stock.controller;


import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.Stock4MinuteDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.pojo.vo.PageResult;
import com.itheima.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/stock/export")
    @ApiOperation(value = "股票涨跌榜转换excel")
    public void stockExport(HttpServletResponse response,
     @RequestParam(name="Page",required = false,defaultValue = "1") Integer Page,
     @RequestParam(name="PageSize",required = false,defaultValue = "20")Integer PageSize
    ){
        stockService.stockExport(response,Page,PageSize);
    }

    @GetMapping("/stock/tradevol")
    @ApiOperation(value = "股票国内大盘成交量")
    public R<Map> findVolume(){
        return stockService.findVolume();
    }

    @GetMapping("/stock/updown")
    @ApiOperation(value = "个股分时涨跌幅度统计")
    public R<Map> findStockUpdown(){
        return stockService.findStockUpdown();
    }

    @GetMapping("/stock/screen/time-sharing")
    @ApiOperation(value = "个股分时K线行情功能")
    public R<List<Stock4MinuteDomain>> findStockScreenTimeSharing(@RequestParam("code") String code){
        return  stockService.findStockScreenTimeSharing(code);
    }

    @GetMapping("/stock/screen/dkline")
    @ApiOperation(value = "日K线行情功能")
    public R<List<Stock4MinuteDomain>> findStockScreenDkline(@RequestParam("code") String code){
        return stockService.findStockScreenDkline(code);
    }


}
