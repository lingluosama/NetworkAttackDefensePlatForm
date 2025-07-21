package com.lingluo.attackdefendplatform.model.query.system;

import com.lingluo.attackdefendplatform.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 上传记录分页查询对象
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
@Schema(description ="上传记录查询对象")
@Getter
@Setter
public class UploadsQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

    private String name;
    private String url;
}
