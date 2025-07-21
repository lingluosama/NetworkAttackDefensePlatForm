package com.lingluo.attackdefendplatform.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 上传记录表单对象
 *
 * @author xuanSAMA
 * @since 2024-09-06 16:44
 */
@Getter
@Setter
@Schema(description = "上传记录表单对象")
public class UploadsForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "不能为空")
    private LocalDateTime createTime;

    private Integer id;

    @NotBlank(message = "不能为空")
    @Size(max=255, message="长度不能超过255个字符")
    private String name;

    @NotNull(message = "不能为空")
    private LocalDateTime updateTime;

    @NotBlank(message = "不能为空")
    @Size(max=255, message="长度不能超过255个字符")
    private String url;


}
