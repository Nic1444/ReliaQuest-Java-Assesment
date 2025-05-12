package com.reliaquest.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteEmployeeRequest {
    @NotBlank
    @JsonProperty("name")
    private String employeeName;
}
