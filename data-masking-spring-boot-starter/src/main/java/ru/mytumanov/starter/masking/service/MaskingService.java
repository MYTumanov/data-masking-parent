package ru.mytumanov.starter.masking.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.mytumanov.starter.masking.config.MaskingProperties;
import ru.mytumanov.starter.masking.model.MaskType;
import ru.mytumanov.starter.masking.service.strategy.MaskingStrategy;

public class MaskingService {
    private final Map<MaskType, MaskingStrategy> strategyMap;

    private final MaskingProperties properties;

    public MaskingService(List<MaskingStrategy> strategies, MaskingProperties properties) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(MaskingStrategy::getType, Function.identity()));
        this.properties = properties;
    }

    public String mask(String value, MaskType maskType) {
        MaskingStrategy strategy = strategyMap.get(maskType);
        if (strategy == null) {
            return value;
        }
        MaskingProperties.MaskRule rule = properties.getRules().get(maskType.name());
        rule = rule == null ? new MaskingProperties.MaskRule() : rule;

        return strategy.mask(value, rule);
    }
}
