//package io.github.xiaoyureed.raincloud.core.starter.database.converter;
//
//import io.github.xiaoyureed.raincloud.core.common.model.enums.GenderEnum;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//@Converter(autoApply = true)
//public class GenderEnumConverter implements AttributeConverter<GenderEnum, Integer> {
//
//    @Override
//    public Integer convertToDatabaseColumn(GenderEnum attribute) {
//        return attribute.getCode();
//    }
//
//    @Override
//    public GenderEnum convertToEntityAttribute(Integer dbData) {
//        return GenderEnum.of(dbData);
//    }
//
//
//}
