package com.itheima.stock.service;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.resp.R;
import com.itheima.stock.pojo.resp.ResponseCode;
import com.itheima.stock.pojo.vo.PageResult;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.untils.DateTimeUtil;
import jdk.internal.instrumentation.Logger;
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

@Service
@Slf4j
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
            //如果集合为空，响应错误提示信息
            //设置响应数据的编码格式
            response.setCharacterEncoding("utf-8");
            if (CollectionUtils.isEmpty(infos)) {
                //响应提示信息
                R<Object> r = R.error(ResponseCode.NO_RESPONSE_DATA);
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(r));
                return;
            }
            //设置响应excel文件格式类型
            response.setContentType("application/vnd.ms-excel");
            //设置默认的文件名称
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("股票excel", "UTF-8");
            //设置默认文件名称：兼容一些特殊浏览器
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //4.响应excel流
            EasyExcel
                    .write(response.getOutputStream(),StockUpdownDomain.class)
                    .sheet("股票信息")
                    .doWrite(infos);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("当前导出数据异常，当前页：{},每页大小：{},异常信息：{}",page,pageSize,e.getMessage());
        }
    }
}
