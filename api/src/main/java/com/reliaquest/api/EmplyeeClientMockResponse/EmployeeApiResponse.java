package com.reliaquest.api.EmplyeeClientMockResponse;

import com.reliaquest.api.dto.EmployeeDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeApiResponse {
    private List<EmployeeDto> data;
}
