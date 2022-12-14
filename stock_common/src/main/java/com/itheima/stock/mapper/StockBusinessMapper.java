package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.StockBusiness;
import com.itheima.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 19308
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2022-10-20 18:02:11
* @Entity com.itheima.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    List<String> getStockIds();

    void insertData(@Param("infos") List<StockRtInfo> infos);
}
