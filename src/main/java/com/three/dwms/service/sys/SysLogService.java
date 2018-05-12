package com.three.dwms.service.sys;

import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.LogTypeCode;
import com.three.dwms.entity.base.BaseEntity;
import com.three.dwms.entity.sys.SysLog;
import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.repository.sys.SysLogRepository;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.JsonMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by csw on 2018/5/11.
 * Description:
 */
@Service
public class SysLogService {

    @Resource
    private SysLogRepository sysLogRepository;

    public void saveSysLog(BaseEntity before, BaseEntity after, SysLog sysLog) {
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        if (RequestHolder.getCurrentUser() != null) {
            sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        }
        sysLog.setOperateTime(new Date());
        sysLogRepository.save(sysLog);
    }
}
