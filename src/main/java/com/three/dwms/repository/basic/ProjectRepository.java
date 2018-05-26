package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/16.
 * Description:
 */
public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer> {
    int countByProjectCodeAndIdNot(String projectCode, Integer id);

    int countByProjectCode(String projectCode);

    int countByProjectNameAndIdNot(String projectName, Integer id);

    int countByProjectName(String projectName);

    @Query("select max(p.projectCode) from Project p")
    String findMaxProjectCode();
}
