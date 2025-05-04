package com.reliaquest.api.dto.EmplyeeClientMockResponse;

import com.reliaquest.api.dto.response.EmployeeDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListApiResponse {
    private List<EmployeeDto> data;
}
