package com.lingluo.attackdefendplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.lingluo.attackdefendplatform.model.entity.Uploads;
import com.lingluo.attackdefendplatform.model.query.system.UploadsQuery;
import com.lingluo.attackdefendplatform.model.vo.system.UploadsVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 上传记录Mapper接口
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
@Mapper
public interface UploadsMapper extends BaseMapper<Uploads> {

    /**
     * 获取上传记录分页数据
     *
     * @param page        分页对象
     * @param queryParams 查询参数
     * @return
     */
    Page<UploadsVO> getuploadsPage(Page<UploadsVO> page, UploadsQuery queryParams);
    Long insertUpload(Uploads entity);
}
