package com.pcz.permission.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.pcz.permission.dao.SysAclMapper;
import com.pcz.permission.dao.SysAclModuleMapper;
import com.pcz.permission.dao.SysDeptMapper;
import com.pcz.permission.dto.AclDto;
import com.pcz.permission.dto.AclModuleLevelDto;
import com.pcz.permission.dto.DeptLevelDto;
import com.pcz.permission.model.SysAcl;
import com.pcz.permission.model.SysAclModule;
import com.pcz.permission.model.SysDept;
import com.pcz.permission.service.SysCoreService;
import com.pcz.permission.service.SysTreeService;
import com.pcz.permission.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author picongzhi
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysCoreService sysCoreService;

    private Comparator<DeptLevelDto> deptSeqComparator = Comparator.comparingInt(SysDept::getSeq);

    private Comparator<AclModuleLevelDto> aclModuleSeqComparator = Comparator.comparingInt(SysAclModule::getSeq);

    private Comparator<AclDto> aclDtoComparator = Comparator.comparingInt(AclDto::getSeq);

    @Override
    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.selectAllDept();
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
                deptLevelDto.setDeptList(tempDeptList);
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    @Override
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.selectAllAclModule();
        List<AclModuleLevelDto> aclModuleLevelDtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList) {
            AclModuleLevelDto aclModuleLevelDto = AclModuleLevelDto.adapt(aclModule);
            aclModuleLevelDtoList.add(aclModuleLevelDto);
        }

        return aclModuleListToTree(aclModuleLevelDtoList);
    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> aclModuleLevelDtoList) {
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return Lists.newArrayList();
        }

        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {
            levelAclModuleMap.put(aclModuleLevelDto.getLevel(), aclModuleLevelDto);
            if (LevelUtil.ROOT.equals(aclModuleLevelDto.getLevel())) {
                rootList.add(aclModuleLevelDto);
            }
        }

        Collections.sort(rootList, aclModuleSeqComparator);
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);

        return rootList;
    }

    protected void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelDtoList, String level, Multimap<String, AclModuleLevelDto> levelAclModuleMap) {
        for (int i = 0; i < aclModuleLevelDtoList.size(); i++) {
            AclModuleLevelDto aclModuleLevelDto = aclModuleLevelDtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
            List<AclModuleLevelDto> tempAclModuleList = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempAclModuleList)) {
                Collections.sort(tempAclModuleList, aclModuleSeqComparator);
                aclModuleLevelDto.setAclModuleList(tempAclModuleList);
                transformAclModuleTree(tempAclModuleList, nextLevel, levelAclModuleMap);
            }
        }
    }

    @Override
    public List<AclModuleLevelDto> roleTree(int roleId) {
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

        Set<Integer> userAclIdSet = userAclList.stream()
                .map(sysAcl -> sysAcl.getId())
                .collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream()
                .map(sysAcl -> sysAcl.getId())
                .collect(Collectors.toSet());

        List<SysAcl> allAclList = sysAclMapper.getAll();
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl sysAcl : allAclList) {
            AclDto aclDto = AclDto.adapt(sysAcl);
            if (userAclIdSet.contains(sysAcl.getId())) {
                aclDto.setHasAcl(true);
            }

            if (roleAclIdSet.contains(sysAcl.getId())) {
                aclDto.setChecked(true);
            }

            aclDtoList.add(aclDto);
        }

        return aclListToTree(aclDtoList);
    }

    @Override
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }

        List<AclModuleLevelDto> aclModuleLevelDtoList = aclModuleTree();
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        bindAclsWithOrder(aclModuleLevelDtoList, moduleIdAclMap);

        return aclModuleLevelDtoList;
    }

    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelDtoList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return;
        }

        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(aclModuleLevelDto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                Collections.sort(aclDtoList, aclDtoComparator);
                aclModuleLevelDto.setAclList(aclDtoList);
            }

            bindAclsWithOrder(aclModuleLevelDto.getAclModuleList(), moduleIdAclMap);
        }
    }

    @Override
    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> sysAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl sysAcl : sysAclList) {
            AclDto aclDto = AclDto.adapt(sysAcl);
            aclDto.setHasAcl(true);
            aclDto.setChecked(true);
            aclDtoList.add(aclDto);
        }

        return aclListToTree(aclDtoList);
    }
}
