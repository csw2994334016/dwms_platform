package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysAcl;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.AclParam;
import com.three.dwms.param.sys.AclTree;
import com.three.dwms.repository.sys.SysAclRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.acl.Acl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2018/5/10.
 * Description:
 */
@Service
@Slf4j
public class SysAclService {

    @Resource
    private SysAclRepository sysAclRepository;

    @Transactional
    public void create(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同级权限下面存在相同名称的权限");
        }
        if (param.getParentId() > 0) {
            SysAcl parentAcl = sysAclRepository.findOne(param.getParentId());
            Preconditions.checkNotNull(parentAcl, "上级权限不存在，可以选择一级权限");
        }

        SysAcl sysAcl = SysAcl.builder().name(param.getName()).parentId(param.getParentId()).type(param.getType()).icon(param.getIcon()).url(param.getUrl()).seq(param.getSeq()).build();

        sysAcl.setStatus(param.getStatus());
        sysAcl.setRemark(param.getRemark());
        sysAcl.setCreator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setCreateTime(new Date());
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAcl.setOperateTime(new Date());

        sysAclRepository.save(sysAcl);
    }

    public void updateStateById(int id, StateCode stateCode) {
        int count = sysAclRepository.countByParentId(id);
        if (count > 0) {
            throw new ParamException("待操作的权限有下级权限，请先操作下级权限");
        }

        SysAcl sysAcl = sysAclRepository.findOne(id);
        Preconditions.checkNotNull(sysAcl, "权限不存在");

        sysAcl.setStatus(stateCode.getCode());
        sysAclRepository.save(sysAcl);
    }

    public SysAcl update(AclParam param) {
        BeanValidator.check(param);
        SysAcl before = sysAclRepository.findOne(param.getId());
        Preconditions.checkNotNull(before, "权限不存在");
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同级权限下面存在相同名称的权限");
        }
        if (param.getParentId() > 0) {
            SysAcl parentAcl = sysAclRepository.findOne(param.getParentId());
            Preconditions.checkNotNull(parentAcl, "上级权限不存在，可以选择一级权限");
        }

        SysAcl after = SysAcl.builder().name(param.getName()).parentId(param.getParentId()).type(param.getType()).icon(param.getIcon()).url(param.getUrl()).seq(param.getSeq()).build();
        after.setId(param.getId());

//        before.setName(param.getName());
//        before.setParentId(param.getParentId());
//        before.setType(param.getType());
//        before.setIcon(param.getIcon());
//        before.setUrl(param.getUrl());
//        before.setSeq(param.getSeq());

        after.setStatus(param.getStatus());
        after.setRemark(param.getRemark());
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        return sysAclRepository.save(after);
    }

    public List<SysAcl> findAll() {
        return (List<SysAcl>) sysAclRepository.findAll();
    }

    public List<AclTree> findAllByTree() {
        List<SysAcl> sysAclList = this.findAll();
        return convertToAclTree(sysAclList);
    }

    public SysAcl findById(int id) {
        SysAcl sysAcl = sysAclRepository.findOne(id);
        Preconditions.checkNotNull(sysAcl, "权限不存在");
        return sysAcl;
    }

    private boolean checkExist(Integer parentId, String name, Integer id) {
        if (id != null) {
            return sysAclRepository.countByParentIdAndNameAndIdNot(parentId, name, id) > 0;
        }
        return sysAclRepository.countByParentIdAndName(parentId, name) > 0;
    }

    private List<AclTree> convertToAclTree(List<SysAcl> sysAclList) {
        Map<Integer, AclTree> tempMap = Maps.newHashMap(); //<aclId, AclTree>
        for (SysAcl sysAcl : sysAclList) {
            AclTree aclTree = AclTree.builder().id(sysAcl.getId()).name(sysAcl.getName()).build();
            if (sysAcl.getParentId() == 0) { //是一级权限
                tempMap.put(sysAcl.getId(), aclTree);
            } else {
                if (tempMap.get(sysAcl.getParentId()) != null) { //上级权限就是一级权限
                    tempMap.get(sysAcl.getParentId()).getChildren().add(aclTree);
                } else { //否则查找上级权限
                    AclTree parentAclTree = findParentAclTree(sysAcl.getParentId(), tempMap);
                    if (parentAclTree != null) {
                        parentAclTree.getChildren().add(aclTree);
                    } else {
                        log.error("权限(name: " + sysAcl.getName() + ")数据错误：不是一级权限找，不到上级权限");
                    }
                }
            }
        }
        return Lists.newArrayList(tempMap.values());
    }

    private AclTree findParentAclTree(Integer parentId, Map<Integer, AclTree> tempMap) {
        AclTree parentAclTree;
        for (Map.Entry<Integer, AclTree> entry : tempMap.entrySet()) {
            for (AclTree child : entry.getValue().getChildren()) {
                parentAclTree = recursion(parentId, child);
                if (parentAclTree != null) {
                    return parentAclTree;
                }
            }
        }
        return null;
    }

    private AclTree recursion(Integer id, AclTree aclTree) {
        if (aclTree.getId().equals(id)) {
            return aclTree;
        }
        for (AclTree child : aclTree.getChildren()) {
            if (child.getId().equals(id)) {
                return child;
            } else {
                recursion(id, child);
            }
        }
        return null;
    }
}
