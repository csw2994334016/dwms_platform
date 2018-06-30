package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.entity.sys.SysNotice;
import com.three.dwms.param.sys.NoticeParam;
import com.three.dwms.repository.sys.SysNoticeRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/6/29.
 * Description:
 */
@Service
public class SysNoticeService {

    @Resource
    private SysNoticeRepository sysNoticeRepository;

    public void create(NoticeParam param) {
        BeanValidator.check(param);

        SysNotice sysNotice = SysNotice.builder().title(param.getTitle()).content(param.getContent()).noticeType(param.getNoticeType()).build();
        sysNotice.setStatus(param.getStatus());
        sysNotice.setRemark(param.getRemark());

        sysNotice.setCreateTime(new Date());
        sysNotice.setCreator(RequestHolder.getCurrentUser().getUsername());
        sysNotice.setOperateTime(new Date());
        sysNotice.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysNotice.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysNoticeRepository.save(sysNotice);
    }

    public void deleteByIds(List<Integer> ids) {
        List<SysNotice> sysNoticeList = Lists.newArrayList();
        for (Integer id : ids) {
            SysNotice sysNotice = this.findById(id);
            sysNoticeList.add(sysNotice);
        }
        sysNoticeRepository.delete(sysNoticeList);
    }

    public SysNotice update(NoticeParam param) {
        SysNotice sysNotice = this.findById(param.getId());
        BeanValidator.check(param);

        sysNotice.setTitle(param.getTitle());
        sysNotice.setContent(param.getContent());
        sysNotice.setCreator(RequestHolder.getCurrentUser().getUsername());
        sysNotice.setNoticeType(param.getNoticeType());
        sysNotice.setStatus(param.getStatus());
        sysNotice.setRemark(param.getRemark());

        sysNotice.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysNotice.setOperateTime(new Date());
        sysNotice.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        return sysNoticeRepository.save(sysNotice);
    }

    public List<SysNotice> findAll() {
        return (List<SysNotice>) sysNoticeRepository.findAll();
    }

    public SysNotice findById(Integer id) {
        SysNotice sysNotice = sysNoticeRepository.findOne(id);
        Preconditions.checkNotNull(sysNotice, "通知公告(id:" + id + ")不存在");
        return sysNotice;
    }
}
