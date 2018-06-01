package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Project;
import com.three.dwms.param.basic.ProjectParam;
import com.three.dwms.service.basic.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/projects")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody ProjectParam param) {
        projectService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        projectService.updateStateById(id, StatusCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<ProjectParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (ProjectParam projectParam : paramList) {
            if (projectParam.getId() != null) {
                ids.add(projectParam.getId());
            }
        }
        projectService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody ProjectParam param) {
        param.setId(id);
        return JsonData.success(projectService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Project> projectList = projectService.findAll();
        return JsonData.success(projectList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Project> categoryPage = projectService.findAllByPage(pageQuery);
        return JsonData.success(categoryPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Project project = projectService.findById(id);
        return JsonData.success(project);
    }
}
