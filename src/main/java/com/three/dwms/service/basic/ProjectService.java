package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.BanJi;
import com.three.dwms.entity.basic.Project;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.BanJiParam;
import com.three.dwms.param.basic.ProjectParam;
import com.three.dwms.repository.basic.ProjectRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@Service
public class ProjectService {

    @Resource
    private ProjectRepository projectRepository;

    @Transactional
    public void create(ProjectParam param) {
        BeanValidator.check(param);
        if (checkProjectCodeExist(param.getProjectCode(), param.getId())) {
            throw new ParamException("项目编号已经存在");
        }
        if (checkProjectNameExist(param.getProjectName(), param.getId())) {
            throw new ParamException("项目名称已经存在");
        }

        Project project = Project.builder().projectCode(param.getProjectCode()).projectName(param.getProjectName()).build();

        project.setStatus(param.getStatus());
        project.setRemark(param.getRemark());
        project.setCreator(RequestHolder.getCurrentUser().getUsername());
        project.setCreateTime(new Date());
        project.setOperator(RequestHolder.getCurrentUser().getUsername());
        project.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        project.setOperateTime(new Date());

        projectRepository.save(project);
    }

    private boolean checkProjectCodeExist(String projectCode, Integer id) {
        if (id != null) {
            return projectRepository.countByProjectCodeAndIdNot(projectCode, id) > 0;
        }
        return projectRepository.countByProjectCode(projectCode) > 0;
    }

    private boolean checkProjectNameExist(String projectName, Integer id) {
        if (id != null) {
            return projectRepository.countByProjectNameAndIdNot(projectName, id) > 0;
        }
        return projectRepository.countByProjectName(projectName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        Project project = this.findById(id);
        //假删除
        project.setStatus(stateCode.getCode());
        projectRepository.save(project);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Project> projects = Lists.newArrayList();
        for (Integer id : ids) {
            Project project = this.findById(id);
            projects.add(project);
        }
        projectRepository.delete(projects);
    }

    @Transactional
    public Project update(ProjectParam param) {
        Project project = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkProjectCodeExist(param.getProjectCode(), param.getId())) {
            throw new ParamException("项目编号已经存在");
        }
        if (checkProjectNameExist(param.getProjectName(), param.getId())) {
            throw new ParamException("项目名称已经存在");
        }

        project.setProjectCode(param.getProjectCode());
        project.setProjectName(param.getProjectName());

        project.setStatus(param.getStatus());
        project.setRemark(param.getRemark());
        project.setOperator(RequestHolder.getCurrentUser().getUsername());
        project.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        project.setOperateTime(new Date());

        return projectRepository.save(project);
    }

    public List<Project> findAll() {
        return (List<Project>) projectRepository.findAll();
    }

    public Page<Project> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return projectRepository.findAll(pageable);
    }

    public Project findById(int id) {
        Project project = projectRepository.findOne(id);
        Preconditions.checkNotNull(project, "项目信息不存在");
        return project;
    }
}
