package com.three.dwms.service.bm;

import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.param.bm.InventoryParam;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.utils.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
@Service
public class InventoryService {

    @Resource
    private InventoryRepository inventoryRepository;

    public List<Inventory> findByWhName(InventoryParam param) {
        BeanValidator.check(param);
        return inventoryRepository.findAllByWhName(param.getWhName());
    }
}
