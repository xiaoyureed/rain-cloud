//package io.github.xiaoyureed.raincloud.core.starter.extension.excel;//package io.github.xiaoyureed.raincloud.core.starter.webextension.excel;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import com.alibaba.excel.converters.Converter;
//import com.alibaba.excel.enums.CellDataTypeEnum;
//import com.alibaba.excel.metadata.GlobalConfiguration;
//import com.alibaba.excel.metadata.data.CellData;
//import com.alibaba.excel.metadata.property.ExcelContentProperty;
//
///**
// * @author liao
// */
//public class LocalDateStringConverter implements Converter<LocalDateTime> {
//
//    @Override
//    public Class<?> supportJavaTypeKey() {
//        return LocalDateTime.class;
//    }
//
//    @Override
//    public CellDataTypeEnum supportExcelTypeKey() {
//        return CellDataTypeEnum.STRING;
//    }
//
//    @Override
//    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
//        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    }
//
//    @Override
//    public CellData<?> convertToExcelData(LocalDateTime localDateTime, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String format = formatter.format(localDateTime);
//        return new CellData<>();
//    }
//}
