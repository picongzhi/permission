package com.pcz.permission.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.pcz.permission.dao.SysDeptMapper;
import com.pcz.permission.dto.DeptLevelDto;
import com.pcz.permission.model.SysDept;
import com.pcz.permission.service.SysTreeService;
import com.pcz.permission.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author picongzhi
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    private Comparator<DeptLevelDto> deptSeqComparator = Comparator.comparingInt(SysDept::getSeq);

    @Override
    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();
        for (SysDept sysDept : deptList) {
            DeptLevelDto deptLevelDto = DeptLevelDto.adapt(sysDept);
            deptLevelDtoList.add(deptLevelDto);
        }

        return deptListToTree(deptLevelDtoList);
    }

    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtoList) {
        if (CollectionUtils.isEmpty(deptLevelDtoList)) {
            return Lists.newArrayList();
        }

        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();

        for (DeptLevelDto deptLevelDto : deptLevelDtoList) {
            levelDeptMap.put(deptLevelDto.getLevel(), deptLevelDto);
            if (LevelUtil.ROOT.equals(deptLevelDto.getLevel())) {
                rootList.add(deptLevelDto);
            }
        }

        Collections.sort(rootList, deptSeqComparator);
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);

        return rootList;
    }

    private void transformDeptTree(List<DeptLevelDto> deptLevelDtoList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
        for (int i = 0; i < deptLevelDtoList.size(); i++) {
            DeptLevelDto deptLevelDto = deptLevelDtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                Collections.sort(tempDeptList, deptSeqComparator);
                deptLevelDto.setDetpList(tempDeptList);
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }
}
