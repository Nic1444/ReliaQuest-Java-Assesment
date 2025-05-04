package com.reliaquest.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEmployeeRequest {

    @NotBlank
    @JsonProperty("name")
    private String employeeName;

    @NotNull @Min(1)
    @JsonProperty("salary")
    private Integer employeeSalary;

    @NotNull @Min(16)
    @Max(75)
    @JsonProperty("age")
    private Integer employeeAge;

    @NotBlank
    @JsonProperty("title")
    private String employeeTitle;
}
