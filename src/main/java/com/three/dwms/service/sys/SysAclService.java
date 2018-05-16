package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysAcl;
import com.three.dwms.entity.sys.SysRoleAcl;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.AclParam;
import com.three.dwms.param.sys.AclTree;
import com.three.dwms.repository.sys.SysAclRepository;
import com.three.dwms.repository.sys.SysRoleAclRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by csw on 2018/5/10.
 * Description:
 */
@Service
@Slf4j
public class SysAclService {

    @Resource
    private SysAclRepository sysAclRepository;

    @Resource
    private SysRoleAclRepository sysRoleAclRepository;

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

    public void deleteByIds(List<Integer> ids) {
        List<SysAcl> sysAcls = Lists.newArrayList();
        for (Integer id : ids) {
            sysAcls.add(this.findById(id));
        }
        sysAclRepository.delete(sysAcls);
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
        List<Sort.Order> orders = Lists.newArrayList();
        orders.add(new Sort.Order(Sort.Direction.ASC,"parentId"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"seq"));
        Sort sorts = new Sort(orders);
        return (List<SysAcl>) sysAclRepository.findAll(sorts);
    }

    public List<SysAcl> findAllByRole(Integer id) {
        List<SysAcl> sysAclList = this.findAll();
        for (SysAcl sysAcl : sysAclList) {
            sysAcl.setChecked(sysRoleAclRepository.countByRoleIdAndAclId(id, sysAcl.getId()) > 0);
        }
        return sysAclList;
    }

    public List<Integer> findAllByRoles(int id) {
        List<SysRoleAcl> sysRoleAclList = sysRoleAclRepository.findAllByRoleId(id);
        List<Integer> aclIdList = Lists.newArrayList();
        for (SysRoleAcl sysRoleAcl : sysRoleAclList) {
            aclIdList.add(sysRoleAcl.getAclId());
        }
        return aclIdList;
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
        List<AclTree> aclTreeList = Lists.newArrayList();
        for (SysAcl sysAcl : sysAclList) {
            AclTree aclTree = AclTree.builder().id(sysAcl.getId()).parentId(sysAcl.getParentId()).name(sysAcl.getName()).seq(sysAcl.getSeq()).build();
            aclTreeList.add(aclTree);
        }
        for (AclTree aclTree : aclTreeList) {
            if (aclTree.getParentId() == 0) { //定义parentId=0是一级权限
                tempMap.put(aclTree.getId(), aclTree);
            } else {
                if (tempMap.get(aclTree.getParentId()) != null) { //父级权限就是一级权限
                    tempMap.get(aclTree.getParentId()).getChildren().add(aclTree);
                } else {
                    AclTree parentAclTree = findParentAclTree(aclTree.getParentId(), tempMap); //在一级权限的子节点逐级往下查找父级权限
                    if (parentAclTree != null) {
                        parentAclTree.getChildren().add(aclTree);
                    } else { //有可能父级权限还没有绑定到tempMap里，在aclTreeList里查找父级权限进行绑定
                        parentAclTree = recursion(aclTree.getParentId(), aclTreeList);
                        if (parentAclTree != null) {
                            parentAclTree.getChildren().add(aclTree);
                        } else {
                            log.error("权限(name: " + aclTree.getName() + ")数据错误：不是一级权限，没有上级权限");
                        }
                    }
                }
            }
        }
        //对树进行排序
        aclTreeList = Lists.newArrayList(tempMap.values());
        recursionSort(aclTreeList);
        return aclTreeList;
    }

    private void recursionSort(List<AclTree> aclTreeList) {
        sort(aclTreeList);
        for (AclTree aclTree : aclTreeList) {
            recursionSort(aclTree.getChildren());
        }
    }


    private void sort(List<AclTree> aclTreeList) {
        aclTreeList.sort((o1, o2) -> {
            if (o1.getParentId().compareTo(o2.getParentId()) == 0) {
                return o1.getSeq().compareTo(o2.getSeq());
            }
            return o1.getParentId().compareTo(o2.getParentId());
        });
    }

    private AclTree findParentAclTree(Integer parentId, Map<Integer, AclTree> tempMap) {
        AclTree parentAclTree;
        for (Map.Entry<Integer, AclTree> entry : tempMap.entrySet()) {
            parentAclTree = recursion(parentId, entry.getValue().getChildren());
            if (parentAclTree != null) {
                return parentAclTree;
            }
        }
        return null;
    }

    private AclTree recursion(Integer id, List<AclTree> aclTreeList) {
        for (AclTree aclTree : aclTreeList) {
            if (aclTree.getId().equals(id)) {
                return aclTree;
            } else {
                recursion(id, aclTree.getChildren());
            }
        }
        return null;
    }
}
