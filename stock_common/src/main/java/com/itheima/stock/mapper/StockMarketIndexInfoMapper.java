package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author 19308
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2022-10-20 18:02:11
* @Entity com.itheima.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    List<InnerMarketDomain> findStockMarket(@Param("code") List<String> inner,@Param("timepiont") Date date);

    List<StockBlockDomain> findStockBlock(@Param("date")Date date);


    List<StockUpdownDomain> findByPageStock(@Param("date")Date date);
}
