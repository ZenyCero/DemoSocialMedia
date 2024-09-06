package org.zerocool.sharedlibrary.mapper;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public static <S, D> D convertToOtherClass(S source, Class<D> destinationType) {
        try {
            D destination = destinationType.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, destination);
            return destination;
        } catch (Exception e) {
            throw new RuntimeException("Error during object mapping", e);
        }
    }
}
