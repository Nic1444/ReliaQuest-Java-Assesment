package com.reliaquest.api.service;

import com.reliaquest.api.client.EmployeeMockServiceClient;
import com.reliaquest.api.dto.request.CreateEmployeeRequest;
import com.reliaquest.api.dto.response.EmployeeDto;
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

    public ResponseEntity<EmployeeDto> getEmployeeById(String id) {
        return this.employeeMockServiceClient.getEmployeeById(id);
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return this.employeeMockServiceClient.getHighestSalaryOfEmployees();
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return this.employeeMockServiceClient.getTopTenHighestEarningEmployeeNames();
    }

    public ResponseEntity<EmployeeDto> createEmployee(CreateEmployeeRequest empRequest) {
        return this.employeeMockServiceClient.createEmployee(empRequest);
    }

    public ResponseEntity<String> deleteEmployeeById(String id) {
        return this.employeeMockServiceClient.deleteEmployeeById(id);
    }
}
