package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.basic.Zone;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
public interface ZoneRepository extends PagingAndSortingRepository<Zone, Integer> {
    int countByZoneCodeAndIdNot(String zoneCode, Integer id);

    int countByZoneCode(String zoneCode);

    int countByZoneNameAndIdNot(String whName, Integer id);

    int countByZoneName(String zoneName);

    List<Zone> findAllByWarehouse(Warehouse warehouse);

    int countByZoneCodeAndWarehouseAndIdNot(String zoneCode, Warehouse warehouse, Integer id);

    int countByZoneCodeAndWarehouse(String zoneCode, Warehouse warehouse);

    int countByZoneNameAndWarehouseAndIdNot(String zoneName, Warehouse warehouse, Integer id);

    int countByZoneNameAndWarehouse(String zoneName, Warehouse warehouse);
}
