package com.three.dwms.service.sys;

import com.google.common.collect.Lists;
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
import java.util.List;

/**
 * Created by csw on 2018/5/11.
 * Description:
 */
@Service
public class SysLogService {

    @Resource
    private SysLogRepository sysLogRepository;

    public void saveSysLog(BaseEntity before, BaseEntity after, SysLog sysLog) {
        sysLog.setTargetId(after != null ? after.getId() : before.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        if (RequestHolder.getCurrentUser() != null) {
            sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        }
        sysLog.setOperateTime(new Date());
        sysLogRepository.save(sysLog);
    }

    public void saveDeleteLog(List<Integer> before, SysLog sysLog) {
        sysLog.setTargetId(null);
        sysLog.setOldValue(JsonMapper.obj2String(before));
        sysLog.setNewValue(JsonMapper.obj2String(Lists.newArrayList()));
        if (RequestHolder.getCurrentUser() != null) {
            sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        }
        sysLog.setOperateTime(new Date());
        sysLogRepository.save(sysLog);
    }
}
