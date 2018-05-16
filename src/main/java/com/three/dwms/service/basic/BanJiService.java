package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.BanJi;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.BanJiParam;
import com.three.dwms.repository.basic.BanJIRepository;
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
public class BanJiService {

    @Resource
    private BanJIRepository banJIRepository;

    @Transactional
    public void create(BanJiParam param) {
        BeanValidator.check(param);
        if (checkBanJiCodeExist(param.getBanJiCode(), param.getId())) {
            throw new ParamException("班级编号已经存在");
        }
        if (checkBanJiNameExist(param.getBanJiName(), param.getId())) {
            throw new ParamException("班级名称已经存在");
        }

        BanJi banJi = BanJi.builder().banJiCode(param.getBanJiCode()).banJiName(param.getBanJiName()).build();

        banJi.setStatus(param.getStatus());
        banJi.setRemark(param.getRemark());
        banJi.setCreator(RequestHolder.getCurrentUser().getUsername());
        banJi.setCreateTime(new Date());
        banJi.setOperator(RequestHolder.getCurrentUser().getUsername());
        banJi.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        banJi.setOperateTime(new Date());

        banJIRepository.save(banJi);
    }

    private boolean checkBanJiCodeExist(String banJiCode, Integer id) {
        if (id != null) {
            return banJIRepository.countByBanJiCodeAndIdNot(banJiCode, id) > 0;
        }
        return banJIRepository.countByBanJiCode(banJiCode) > 0;
    }

    private boolean checkBanJiNameExist(String banJiName, Integer id) {
        if (id != null) {
            return banJIRepository.countByBanJiNameAndIdNot(banJiName, id) > 0;
        }
        return banJIRepository.countByBanJiName(banJiName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        BanJi banJi = this.findById(id);
        //假删除
        banJi.setStatus(stateCode.getCode());
        banJIRepository.save(banJi);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<BanJi> banJis = Lists.newArrayList();
        for (Integer id : ids) {
            BanJi banJi = this.findById(id);
            banJis.add(banJi);
        }
        banJIRepository.delete(banJis);
    }

    public BanJi update(BanJiParam param) {
        BanJi banJi = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkBanJiCodeExist(param.getBanJiCode(), param.getId())) {
            throw new ParamException("班级编号已经存在");
        }
        if (checkBanJiNameExist(param.getBanJiName(), param.getId())) {
            throw new ParamException("班级名称已经存在");
        }

        banJi.setBanJiCode(param.getBanJiCode());
        banJi.setBanJiName(param.getBanJiName());

        banJi.setStatus(param.getStatus());
        banJi.setRemark(param.getRemark());
        banJi.setOperator(RequestHolder.getCurrentUser().getUsername());
        banJi.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        banJi.setOperateTime(new Date());

        return banJIRepository.save(banJi);
    }

    public List<BanJi> findAll() {
        return (List<BanJi>) banJIRepository.findAll();
    }

    public Page<BanJi> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return banJIRepository.findAll(pageable);
    }

    public BanJi findById(int id) {
        BanJi banJi = banJIRepository.findOne(id);
        Preconditions.checkNotNull(banJi, "班级信息不存在");
        return banJi;
    }
}
