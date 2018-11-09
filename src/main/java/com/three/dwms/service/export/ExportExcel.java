package com.three.dwms.service.export;

import com.three.dwms.utils.ExcelWaterUtil;
import com.three.dwms.utils.ImageUtil;
import com.three.dwms.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.util.UUID;

/**
 * Created by csw on 2018/11/8.
 * Description:
 */
@Slf4j
public class ExportExcel {

    public String export(ExportData exportData) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("物料导出表");
        // 为文件添加密码，设置文件只读
        sheet.protectSheet(UUID.randomUUID().toString());
        // 标题，在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow row1 = sheet.createRow(0);
        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        HSSFCell cell = row1.createCell(0);
        // 设置单元格内容
        cell.setCellValue(exportData.getTitle());
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        // 标题字体
        HSSFFont titleFont = wb.createFont();
        titleFont.setFontName("宋体");
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 粗体显示
        titleFont.setFontHeightInPoints((short) 16);
        // 标题样式
        HSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setFont(titleFont); // 调用字体样式对象
        titleStyle.setWrapText(false);
        // 设置样式
        cell.setCellStyle(titleStyle);

        // 表头，在sheet里创建第二行
        HSSFRow row2 = sheet.createRow(1);
        // 创建单元格并设置单元格内容
        row2.createCell(0).setCellValue("库房名称");
        row2.createCell(1).setCellValue("物料名称");
        row2.createCell(2).setCellValue("规格/品牌/型号");
        row2.createCell(3).setCellValue("单位");
        row2.createCell(4).setCellValue("单价");
        row2.createCell(5).setCellValue("数量");
        row2.createCell(6).setCellValue("价格总计");
        row2.createCell(7).setCellValue("采购人");
        row2.createCell(8).setCellValue("导出日期");
        // 表头字体
        HSSFFont headFont = wb.createFont();
        headFont.setFontName("宋体");
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 粗体显示
        headFont.setFontHeightInPoints((short) 12);
        // 标题样式
        HSSFCellStyle headStyle = wb.createCellStyle();
        headStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setFont(headFont); // 调用字体样式对象
        headStyle.setWrapText(false);
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        // 设置样式
        Integer[] width = new Integer[]{12, 12, 17, 5, 5, 5, 11, 8, 11};
        for (int i = 0; i < 9; i++) {
            row2.getCell(i).setCellStyle(headStyle);
            // 设置列宽
            sheet.setColumnWidth(i, width[i] * 256);
            row2.getCell(i).setCellStyle(headStyle);
        }

        // 在sheet里创建第三行
        int n = 2 + exportData.getRecordList().size();
        for (int i = 0; i < exportData.getRecordList().size(); i++) {
            Record record = exportData.getRecordList().get(i);
            HSSFRow row3 = sheet.createRow(i + 2);
            row3.createCell(0).setCellValue(record.getWhName());
            row3.createCell(1).setCellValue(record.getSkuDesc());
            row3.createCell(2).setCellValue(record.getSpec());
            row3.createCell(3).setCellValue(record.getUnitName());
            row3.createCell(4).setCellValue(record.getUnitPrice());
            row3.createCell(5).setCellValue(record.getAmount());
            row3.createCell(6).setCellValue(record.getTotalPrice());
            row3.createCell(7).setCellValue(record.getPurchaser());
            row3.createCell(8).setCellValue(record.getCreateTime());
        }
        // 合计行
        HSSFRow row4 = sheet.createRow(n);
        HSSFCell cel41 = row4.createCell(0);
        cel41.setCellValue("合计");
        HSSFCell cel42 = row4.createCell(6);
        cel42.setCellValue(exportData.getTotalMoney());
        row4.createCell(1);
        row4.createCell(2);
        row4.createCell(3);
        row4.createCell(4);
        row4.createCell(5);
        row4.createCell(7);
        row4.createCell(8);
        sheet.addMergedRegion(new CellRangeAddress(n, n, 0, 5));
        // 设置内容样式
        HSSFFont cellFont = wb.createFont();
        cellFont.setFontName("宋体");
//        cellFont.setBold(false); // 不显示粗体
        cellFont.setFontHeightInPoints((short) 12);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(false);
        cellStyle.setFont(cellFont);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < 9; j++) {
                sheet.getRow(i).getCell(j).setCellStyle(cellStyle);
            }
        }

        // 签字第一行
        n++;
        sheet.createRow(n);
        n++;
        HSSFRow row5 = sheet.createRow(n);
        row5.createCell(0).setCellValue("管理员签字：");
        sheet.addMergedRegion(new CellRangeAddress(n, n, 0, 1));
        row5.createCell(3).setCellValue("采购部门主任签字：");
        sheet.addMergedRegion(new CellRangeAddress(n, n, 3, 6));
        // 签字第二行
        n++;
        sheet.createRow(n);
        n++;
        HSSFRow row6 = sheet.createRow(n);
        row6.createCell(0).setCellValue("实训中心主任签字：");
        sheet.addMergedRegion(new CellRangeAddress(n, n, 0, 1));
        row6.createCell(3).setCellValue("分管校长签字：");
        sheet.addMergedRegion(new CellRangeAddress(n, n, 3, 6));
        // 设置内容样式
        HSSFFont cellFont1 = wb.createFont();
        cellFont1.setFontName("宋体");
//        cellFont1.setBold(false); // 粗体显示
        cellFont1.setFontHeightInPoints((short) 14);
        HSSFCellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle1.setWrapText(false);
        cellStyle1.setFont(cellFont1);
        row5.getCell(0).setCellStyle(cellStyle1);
        row5.getCell(3).setCellStyle(cellStyle1);
        row6.getCell(0).setCellStyle(cellStyle1);
        row6.getCell(3).setCellStyle(cellStyle1);

        // 设置行高
        for (int i = 1; i < n; i++) {
            sheet.getRow(i).setHeightInPoints(15);
        }

        // 生成水印图片
        String waterContent = exportData.getWaterContent();
        String path = System.getProperty("catalina.home");
        String imgPath = path + "\\dwms\\water\\" + waterContent + ".png";
        try {
            ImageUtil.createWaterMark(waterContent, imgPath);
        } catch (IOException e) {
            log.error("生成水印图片异常！");
            e.printStackTrace();
        }

        int count = n / 50;
        count = (count + 1) * 4;

        // 生成Excel水印
        try {
            ExcelWaterUtil.putWaterRemarkToExcel(wb, sheet, imgPath, 0, 0, 0, 10, 1, count, 0, 0);
        } catch (IOException e) {
            log.error("生成Excel水印异常！");
            e.printStackTrace();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        wb.close();
        byte[] content = os.toByteArray();

        String fileName = exportData.getPurchaser() + StringUtil.getCurDateStrByPattern("yyyyMMddHHmmss");
        String filePath = path + "\\dwms\\exportFile\\" + fileName + ".xls";
        File file1 = new File(filePath);// Excel文件生成后存储的位置
        OutputStream fos;
        try {
            fos = new FileOutputStream(file1);
            fos.write(content);
            os.close();
            fos.close();
        } catch (Exception e) {
            log.error("生成导出文件异常！");
            e.printStackTrace();
        }
        return fileName + ".xls";
    }
}
