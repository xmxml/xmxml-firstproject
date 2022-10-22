package com.itheima.stock.service;

import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.pojo.resp.ResponseCode;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.untils.DateTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class StockService {
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    public R<List<InnerMarketDomain>> findStockMarket() {
        Date date = dateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        date = DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<String> inner = stockInfoConfig.getInner();
        List<InnerMarketDomain> stockMarket = stockMarketIndexInfoMapper.findStockMarket(inner, date);
        return R.ok(stockMarket);
    }

    public R<List<StockBlockDomain>> findStockBlock() {
        Date date = dateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        date = DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockBlockDomain> stockBlock = stockMarketIndexInfoMapper.findStockBlock(date);
        if (CollectionUtils.isEmpty(stockBlock)){
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        return R.ok(stockBlock);
    }
}
