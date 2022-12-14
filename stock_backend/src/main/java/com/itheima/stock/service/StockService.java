package com.itheima.stock.service;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.Stock4MinuteDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.pojo.resp.ResponseCode;
import com.itheima.stock.pojo.vo.PageResult;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.untils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockService {
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

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


    public R<PageResult<StockUpdownDomain>> findByPageStock(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Date date = DateTimeUtil.getLastDate4Stock(new DateTime()).toDate();
        date = DateTime.parse("2022-06-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> list = stockMarketIndexInfoMapper.findByPageStock(date);
        PageInfo<StockUpdownDomain> stockUpdownDomainPageInfo = new PageInfo<>(list);
        PageResult<StockUpdownDomain> stockUpdownDomainPageResult = new PageResult<>(stockUpdownDomainPageInfo);
        return R.ok(stockUpdownDomainPageResult);
    }

    public R<List<StockUpdownDomain>> findStockGain() {
        Date date = DateTimeUtil.getLastDate4Stock(new DateTime()).toDate();
        date = DateTime.parse("2022-06-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> list = stockMarketIndexInfoMapper.findStockGain(date);
        return R.ok(list);
    }

    public R<Map> findStockUpdownCount() {
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(new DateTime());
        Date opendate = lastDate4Stock.toDate();
        opendate = DateTime.parse("2022-01-06 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date closedate = DateTimeUtil.getOpenDate(lastDate4Stock).toDate();
        closedate = DateTime.parse("2022-01-06 14:25:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> opencount = stockMarketIndexInfoMapper.findStockUpCount(opendate,closedate);
        List<Map> closecount = stockMarketIndexInfoMapper.findStockDownCount(opendate,closedate);
        HashMap<String, List> map = new HashMap<>();
        map.put("upList",opencount);
        map.put("downList",closecount);
        return R.ok(map);
    }

    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        try {
            Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            curDate=DateTime.parse("2022-01-05 09:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            PageHelper.startPage(page,pageSize);
            List<StockUpdownDomain> infos=stockMarketIndexInfoMapper.findByPageStock(curDate);
            //?????????????????????????????????????????????
            //?????????????????????????????????
            response.setCharacterEncoding("utf-8");
            if (CollectionUtils.isEmpty(infos)) {
                //??????????????????
                R<Object> r = R.error(ResponseCode.NO_RESPONSE_DATA);
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(r));
                return;
            }
            //????????????excel??????????????????
            response.setContentType("application/vnd.ms-excel");
            //???????????????????????????
            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
            String fileName = URLEncoder.encode("??????excel", "UTF-8");
            //??????????????????????????????????????????????????????
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //4.??????excel???
            EasyExcel
                    .write(response.getOutputStream(),StockUpdownDomain.class)
                    .sheet("????????????")
                    .doWrite(infos);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("???????????????????????????????????????{},???????????????{},???????????????{}",page,pageSize,e.getMessage());
        }
    }

    public R<Map> findVolume() {
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(new DateTime());
        Date nowDate = lastDate4Stock.toDate();
        Date nowOpenDate = DateTimeUtil.getOpenDate(lastDate4Stock).toDate();
        nowOpenDate=DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        nowDate=DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        DateTime agoDate4Stock = DateTimeUtil.getPreviousTradingDay(lastDate4Stock);
        Date agoDate = agoDate4Stock.toDate();
        Date agoOpenDate = DateTimeUtil.getOpenDate(agoDate4Stock).toDate();
        agoOpenDate=DateTime.parse("2022-01-02 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        agoDate=DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<String> inner = stockInfoConfig.getInner();
        List<Map> nowMap = stockMarketIndexInfoMapper.findVolume(inner,nowDate,nowOpenDate);
        List<Map> agoMap = stockMarketIndexInfoMapper.findVolume(inner,agoDate,agoOpenDate);
        Map<String,List> map = new HashMap<>();
        map.put("amtList",nowMap);
        map.put("yesAmtList",agoMap);
        return R.ok(map);
    }

    public R<Map> findStockUpdown() {
        Date curTime = DateTimeUtil.getLastDate4Stock(new DateTime()).toDate();
        curTime=DateTime.parse("2022-01-06 09:55:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> list=stockRtInfoMapper.findStockUpdown(curTime);
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        List<Map> maps = upDownRange.stream().map(title -> {
            Map map = null;
            Optional<Map> op = list.stream().filter(m -> m.containsValue(title)).findFirst();
            if (op.isPresent()) {
                map = op.get();
            } else {
                map = new HashMap();
                map.put("count", 0);
                map.put("title", title);
            }
            return map;
        }).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        String s = new DateTime(curTime).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
        map.put("time",s);
        map.put("infos",maps);
        return R.ok(map);
    }

    public R<List<Stock4MinuteDomain>> findStockScreenTimeSharing(String code) {
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(new DateTime());
        Date nowDate = lastDate4Stock.toDate();
        Date nowOpenDate = DateTimeUtil.getOpenDate(lastDate4Stock).toDate();
        nowDate=DateTime.parse("2021-12-30 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        nowOpenDate=DateTime.parse("2021-12-30 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Stock4MinuteDomain> list = stockRtInfoMapper.findStockScreenTimeSharing(code,nowDate,nowOpenDate);
        return R.ok(list);
    }

    public R<List<Stock4MinuteDomain>> findStockScreenDkline(String code) {
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(new DateTime());
        Date nowDate = lastDate4Stock.toDate();
        DateTime startDateTime = lastDate4Stock.minusDays(30);
        Date agoDate = startDateTime.toDate();
        nowDate=DateTime.parse("2022-01-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        agoDate=DateTime.parse("2022-01-01 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Stock4MinuteDomain> list = stockRtInfoMapper.findStockScreenDkline(code,nowDate,agoDate);
        return R.ok(list);
    }
}
