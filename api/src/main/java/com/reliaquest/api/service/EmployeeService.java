package com.reliaquest.api.service;

import com.reliaquest.api.client.EmployeeMockServiceClient;
import com.reliaquest.api.dto.EmployeeDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeMockServiceClient employeeMockServiceClient;

    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return this.employeeMockServiceClient.getAllEmployees();
    }

    public ResponseEntity<List<EmployeeDto>> getEmployeesByName(String name) {
        return this.employeeMockServiceClient.getEmployeesByName(name);
    }
}
