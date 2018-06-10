package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysAcl;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/10.
 * Description:
 */
public interface SysAclRepository extends PagingAndSortingRepository<SysAcl, Integer> {

    int countByParentIdAndNameAndIdNot(Integer parentId, String name, Integer id);

    int countByParentIdAndName(Integer parentId, String name);

    int countByParentId(int id);

    SysAcl findByUrlAndMethod(String url, String method);
}
