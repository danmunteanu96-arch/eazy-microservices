package com.eazy.loans.dto;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "loans")
public record LoansConfigPropDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {

}
