package com.lingluo.attackdefendplatform.converter;

import com.lingluo.attackdefendplatform.model.entity.Uploads;
import com.lingluo.attackdefendplatform.model.form.UploadsForm;
import org.mapstruct.Mapper;

/**
 * 上传记录对象转换器
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
@Mapper(componentModel = "spring")
public interface UploadsConverter {

    UploadsForm toForm(Uploads entity);

    Uploads toEntity(UploadsForm formData);
}