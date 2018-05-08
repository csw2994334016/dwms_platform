package com.three.dwms.service.sys;

import org.springframework.stereotype.Service;

/**
 * Created by csw on 2018/5/8.
 * Description:
 */
@Service
public class SysCoreService {

    public boolean hasUrlAcl(String servletPath) {
        return true;
    }
}
