package com.three.dwms.common;

import com.three.dwms.beans.PageQuery;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by csw on 2018/3/23.
 * Description:
 */
public class PageQueryFactory {

    public static PageQuery defaultQuery() {
        PageQuery pageQuery = new PageQuery();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            int limit = Integer.valueOf(request.getParameter("limit")); //每页展示数量pageSize
            int offset = Integer.valueOf(request.getParameter("offset"));
            String sort = request.getParameter("sort"); //按某个字段排序
            String order = request.getParameter("order");
            pageQuery.setPageNo((offset / limit + 1)); //当前页码
            pageQuery.setPageSize(limit);
        }
        return pageQuery;
    }
}
