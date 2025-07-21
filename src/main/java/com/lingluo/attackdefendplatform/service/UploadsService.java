package com.lingluo.attackdefendplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingluo.attackdefendplatform.model.entity.Uploads;
import com.lingluo.attackdefendplatform.model.form.UploadsForm;
import com.lingluo.attackdefendplatform.model.query.system.UploadsQuery;
import com.lingluo.attackdefendplatform.model.vo.system.UploadsVO;

/**
 * 上传记录服务类
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
public interface UploadsService extends IService<Uploads> {

    /**
     *上传记录分页列表
     *
     * @return
     */
    IPage<UploadsVO> getuploadsPage(UploadsQuery queryParams);

    /**
     * 获取上传记录表单数据
     *
     * @param id 上传记录ID
     * @return
     */
     UploadsForm getuploadsFormData(Long id);

    /**
     * 新增上传记录
     *
     * @param formData 上传记录表单对象
     * @return
     */
    boolean saveuploads(UploadsForm formData);

    /**
     * 修改上传记录
     *
     * @param id   上传记录ID
     * @param formData 上传记录表单对象
     * @return
     */
    boolean updateuploads(Long id, UploadsForm formData);

    /**
     * 删除上传记录
     *
     * @param ids 上传记录ID，多个以英文逗号(,)分割
     * @return
     */
    boolean deleteuploadss(String ids);
    Long insertUpload(Uploads entity);
}
