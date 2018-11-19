package com.three.dwms.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/22.
 * Description:
 */
@Slf4j
public class ImportExcel<T> {

    private String dateFormat = "yyyy-MM-dd";

    public List<T> leadInExcel(String fileName, MultipartFile multipartFile, Class<T> clazz, List<String> attributes) {
        List<T> dataList = Lists.newArrayList();
        //把spring文件上传的MultipartFile转换成CommonsMultipartFile类型
        CommonsMultipartFile cmf = (CommonsMultipartFile) multipartFile;
        String path = System.getProperty("catalina.home") + "\\dwms\\uploadFile";
        log.debug("上传文件存放地址：{}", path);
        File filepath = new File(path); //获取本地存储路径
        if (!filepath.exists())
            filepath.mkdirs();
        //新建一个文件
        String extension;
        if (StringUtil.isExcel2003(fileName)) {
            extension = StringUtil.excel2003;
        } else if (StringUtil.isExcel2007(fileName)) {
            extension = StringUtil.excel2007;
        } else {
            return dataList;
        }
        fileName = fileName.replace(extension, "");
        File file = new File(path + "\\" + fileName + StringUtil.getCurDateStrByPattern("yyyyMMddHHmmss") + extension);
        //将上传的文件写入新建的文件中
        try {
            cmf.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化输入流
        InputStream is = null;
        try {
            //根据新建的文件实例化输入流
            is = new FileInputStream(file);
            //根据excel里面的内容读取客户信息
            dataList = readExcelValue(is, extension, clazz, attributes);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }

    public List<T> readExcelValue(InputStream inputStream, String extension, Class<T> clazz, List<String> attributes) throws IllegalAccessException, InstantiationException, InvocationTargetException, ParseException, NoSuchFieldException, NoSuchMethodException, IOException {
        List<T> dataList = Lists.newArrayList();
        Workbook workbook;
        if (StringUtil.excel2003.equals(extension)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (StringUtil.excel2007.equals(extension)) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            return dataList;
        }
        //获取标题行列数
        Sheet sheet = workbook.getSheetAt(0);
        int titleCellNum = sheet.getRow(0).getLastCellNum();
        // 获取值
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            log.debug(row.toString());
            // 1.若当前行的列数不等于标题行列数就放弃整行数据(若想放弃此功能注释4个步骤即可)
            int lastCellNum = row.getLastCellNum();
//            if (titleCellNum != lastCellNum) {
//                continue;
//            }

            // 2.标记
            boolean judge = true;
            T obj = clazz.newInstance();
            for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                Cell cell = row.getCell(columnIndex);
                // 处理单元格中值得类型
                String value = this.getCellValue(cell);

                // 3.单元格中的值等于null或等于"" 就放弃整行数据
                if (value == null || "".equals(value.trim())) {
//                    judge = false;
                    continue;
                }

                Field field = clazz.getDeclaredField(attributes.get(columnIndex));
                Class<?> fieldType = field.getType();
                Object arg = null;
                if (fieldType.isAssignableFrom(Integer.class)) {
                    arg = Integer.valueOf(value);
                } else if (fieldType.isAssignableFrom(Double.class)) {
                    arg = Double.valueOf(value);
                } else if (fieldType.isAssignableFrom(Float.class)) {
                    arg = Float.valueOf(value);
                } else if (fieldType.isAssignableFrom(Long.class)) {
                    arg = Long.valueOf(value);
                } else if (fieldType.isAssignableFrom(Date.class)) {
                    arg = new SimpleDateFormat(dateFormat).parse(value);
                } else if (fieldType.isAssignableFrom(Boolean.class)) {
                    arg = "Y".equals(value) || "1".equals(value);
                } else if (fieldType.isAssignableFrom(String.class)) {
                    arg = value;
                } else {
                    arg = value;
                }
                // char跟byte就不用判断了
                Method method = clazz.getMethod("set" + this.toUpperFirstCase(attributes.get(columnIndex)), fieldType);
                method.invoke(obj, arg);

            }
            // 4. if
            if (judge)
                dataList.add(obj);
        }
        return dataList;
    }

    private String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    //判断是是日期型，转换日期格式，否则转换数字格式。
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date dateCellValue = cell.getDateCellValue();
                        if (dateCellValue != null) {
                            result = new SimpleDateFormat(dateFormat).format(dateCellValue);
                        } else {
                            result = "";
                        }
                    } else {
                        result = new DecimalFormat("#####.##").format(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    private String toUpperFirstCase(String str) {
        return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
    }

}
