package com.example.demo.config;

import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

@Component
public class EnumConverter implements ConverterFactory<String, Enum>{
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum<>(targetType);
    }

    private static class StringToEnum<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            return (T) Enum.valueOf(enumType, source.trim().toUpperCase());
        }
    }
}
