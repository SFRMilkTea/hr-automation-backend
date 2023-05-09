package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.ProductEntity;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExcelService {

    private final ProductService productService;

    public ExcelService(ProductService productService) {
        this.productService = productService;
    }

    public byte[] createExcel() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        String name = String.valueOf(LocalDateTime.now()).split("T")[0];
        HSSFSheet sheet = workbook.createSheet(name);
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        titleStyle.setFont(font);
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        Cell nameCell = row.createCell(0);
        nameCell.setCellStyle(titleStyle);
        nameCell.setCellValue("Название");
        Cell codeCell = row.createCell(1);
        codeCell.setCellStyle(titleStyle);
        codeCell.setCellValue("Артикул");
        Cell quantityCell = row.createCell(2);
        quantityCell.setCellStyle(titleStyle);
        quantityCell.setCellValue("Количество");
        List<ProductEntity> products = productService.getOrderedProducts();
        for (ProductEntity product : products) {
            rowNum++;
            Row row1 = sheet.createRow(rowNum);
            Cell cell0 = row1.createCell(0);
            cell0.setCellValue(product.getName());
            Cell cell1 = row1.createCell(1);
            cell1.setCellValue(product.getCode());
            Cell cell2 = row1.createCell(2);
            cell2.setCellValue(product.getQuantity());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        File f = new File(name.concat(".xls"));
        f.createNewFile();
        FileOutputStream out = new FileOutputStream(f);
        workbook.write(out);

        return FileUtils.readFileToByteArray(f);

    }
}
