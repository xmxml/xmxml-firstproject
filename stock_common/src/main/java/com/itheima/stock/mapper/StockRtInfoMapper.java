package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.Stock4MinuteDomain;
import com.itheima.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 19308
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2022-10-20 18:02:11
* @Entity com.itheima.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    List<Map> findStockUpdown(@Param("curTime") Date curTime);

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    List<Stock4MinuteDomain> findStockScreenTimeSharing(@Param("code") String code, @Param("nowDate") Date nowDate, @Param("nowOpenDate") Date nowOpenDate);

    List<Stock4MinuteDomain> findStockScreenDkline(@Param("code") String code, @Param("nowDate") Date nowDate, @Param("agoDate") Date agoDate);
}
