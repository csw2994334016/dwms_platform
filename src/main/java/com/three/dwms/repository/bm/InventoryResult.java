package com.three.dwms.repository.bm;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
public interface InventoryResult {

    String getWhName();

    String getSku();

    String getSkuDesc();

    String getSpec();

    Double getSkuAmount();

    String setWhName(String whName);

    String setSku(String sku);

    String setSkuDesc(String skuDesc);

    String setSpec(String spec);

    Double setSkuAmount(String skuAmount);

}
