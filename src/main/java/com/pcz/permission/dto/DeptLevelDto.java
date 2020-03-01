package com.pcz.permission.dto;

import com.google.common.collect.Lists;
import com.pcz.permission.model.SysDept;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
    private List<DeptLevelDto> detpList = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept sysDept) {
        DeptLevelDto deptLevelDto = new DeptLevelDto();
        BeanUtils.copyProperties(sysDept, deptLevelDto);

        return deptLevelDto;
    }
}
