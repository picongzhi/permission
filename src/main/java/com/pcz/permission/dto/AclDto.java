package com.pcz.permission.dto;

import com.pcz.permission.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author picongzhi
 */
@Getter
@Setter
@ToString
public class AclDto extends SysAcl {
    /**
     * 是否要默认选中
     */
    private boolean checked = false;

    /**
     * 是否有权限操作
     */
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl sysAcl) {
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(sysAcl, aclDto);

        return aclDto;
    }
}
