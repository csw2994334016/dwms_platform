package com.three.dwms.service.bm;

import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.utils.ImportExcel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Created by csw on 2018/5/22.
 * Description:
 */
@Service
public class InputDetailService {

    public boolean batchImport(String filename, MultipartFile file) {
        boolean leadInOk = false;
        //创建处理EXCEL
        ImportExcel<InputDetail> importExcel = new ImportExcel<>();
        //解析excel，获取客户信息集合
        String[] str = {"inputDate", "whName", "skuDesc", "spec", "unit", "unitPrice", "amount", "totalPrice", "purchaseDept", "purchaser", "receiver", "supplierName", "remark"};
        List<String> attributes = Arrays.asList(str);
        List<InputDetail> inputDetailList = importExcel.leadInExcel(filename, file, InputDetail.class, attributes);

        if (inputDetailList != null) {
            leadInOk = true;
        }

        //存入数据库

        return leadInOk;
    }
}
