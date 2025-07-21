package com.lingluo.attackdefendplatform.model.vo.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 上传记录视图对象
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
@Getter
@Setter
@Schema( description = "上传记录视图对象")
public class UploadsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime createTime;
    private Integer id;
    private String name;
    private LocalDateTime updateTime;
    private String url;
}
