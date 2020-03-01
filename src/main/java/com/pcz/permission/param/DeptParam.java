package com.pcz.permission.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author picongzhi
 */
@Getter
@Setter
@ToString
public class DeptParam {
    private Integer id;

    @NotNull(message = "部门名称不能为空")
    @Length(max = 15, min = 2, message = "部门名称长度需要在2-15个字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;

    @Length(max = 150, message = "备注的长度不能超过150个字符")
    private String remark;
}
