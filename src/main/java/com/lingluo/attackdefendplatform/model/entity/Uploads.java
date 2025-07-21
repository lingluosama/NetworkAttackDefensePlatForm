package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lingluo.attackdefendplatform.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 上传记录实体对象
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
@Getter
@Setter
@TableName("uploads")
public class Uploads extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private String url;
}
