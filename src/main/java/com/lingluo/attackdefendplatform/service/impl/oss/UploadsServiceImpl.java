package com.lingluo.attackdefendplatform.service.impl.oss;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.converter.UploadsConverter;
import com.lingluo.attackdefendplatform.mapper.UploadsMapper;
import com.lingluo.attackdefendplatform.model.entity.Uploads;
import com.lingluo.attackdefendplatform.model.form.UploadsForm;
import com.lingluo.attackdefendplatform.model.query.system.UploadsQuery;
import com.lingluo.attackdefendplatform.model.vo.system.UploadsVO;
import com.lingluo.attackdefendplatform.service.UploadsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 上传记录服务实现类
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
@Service
@RequiredArgsConstructor
public class UploadsServiceImpl extends ServiceImpl<UploadsMapper, Uploads> implements UploadsService {

    private final UploadsConverter uploadsConverter;

    /**
    * 获取上传记录分页列表
    *
    * @param queryParams 查询参数
    * @return {@link IPage< UploadsVO >} 上传记录分页列表
    */
    @Override
    public IPage<UploadsVO> getuploadsPage(UploadsQuery queryParams) {
        Page<UploadsVO> pageVO = this.baseMapper.getuploadsPage(
                new Page<UploadsVO>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }
    
    /**
     * 获取上传记录表单数据
     *
     * @param id 上传记录ID
     * @return
     */
    @Override
    public UploadsForm getuploadsFormData(Long id) {
        Uploads entity = this.getById(id);
        return uploadsConverter.toForm(entity);
    }
    
    /**
     * 新增上传记录
     *
     * @param formData 上传记录表单对象
     * @return
     */
    @Override
    public boolean saveuploads(UploadsForm formData) {
        Uploads entity = uploadsConverter.toEntity(formData);
        return this.save(entity);
    }
    
    /**
     * 更新上传记录
     *
     * @param id   上传记录ID
     * @param formData 上传记录表单对象
     * @return
     */
    @Override
    public boolean updateuploads(Long id, UploadsForm formData) {
        Uploads entity = uploadsConverter.toEntity(formData);
        return this.updateById(entity);
    }
    
    /**
     * 删除上传记录
     *
     * @param ids 上传记录ID，多个以英文逗号(,)分割
     * @return
     */
    @Override
    public boolean deleteuploadss(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的上传记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

    @Override
    public Long insertUpload(Uploads entity) {
        return this.baseMapper.insertUpload(entity);
    }

}
