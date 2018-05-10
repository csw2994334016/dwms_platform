package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysAcl;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.AclParam;
import com.three.dwms.repository.sys.SysAclRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/10.
 * Description:
 */
@Service
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

        before.setName(param.getName());
        before.setParentId(param.getParentId());
        before.setType(param.getType());
        before.setIcon(param.getIcon());
        before.setUrl(param.getUrl());
        before.setSeq(param.getSeq());

        before.setStatus(param.getStatus());
        before.setRemark(param.getRemark());
        before.setOperator(RequestHolder.getCurrentUser().getUsername());
        before.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        before.setOperateTime(new Date());

        return sysAclRepository.save(before);
    }

    public List<SysAcl> findAll() {
        return (List<SysAcl>) sysAclRepository.findAll();
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
}
