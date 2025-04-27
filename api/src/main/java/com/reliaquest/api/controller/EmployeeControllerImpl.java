package com.reliaquest.api.controller;

import com.reliaquest.api.dto.EmployeeDto;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeControllerImpl implements IEmployeeController {

    private final EmployeeService employeeService;

    @Override
    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return this.employeeService.getAllEmployees();
    }

    @Override
    @GetMapping("/search/by-name")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByNameSearch(@RequestParam("name") String name) {

        return this.employeeService.getEmployeesByName(name);
    }

    @Override
    public ResponseEntity getEmployeeById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return null;
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return null;
    }

    @Override
    public ResponseEntity createEmployee(Object employeeInput) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return null;
    }
}
