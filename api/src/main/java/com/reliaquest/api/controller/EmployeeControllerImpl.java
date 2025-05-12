package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.request.CreateEmployeeRequest;
import com.reliaquest.api.dto.response.EmployeeDto;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeControllerImpl implements IEmployeeController {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

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
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {

        return this.employeeService.getEmployeeById(id);
    }

    @Override
    @GetMapping("/highest-salary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return this.employeeService.getHighestSalaryOfEmployees();
    }

    /**
     * Note-
     * In api.README description looks incorrect its saying get 10 Employee objects
     * but in IEmployeeController interface we are returning employeeNames as string
     */
    @Override
    @GetMapping("/top-10-earners")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return this.employeeService.getTopTenHighestEarningEmployeeNames();
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<EmployeeDto> createEmployee(Object employeeInput) {
        CreateEmployeeRequest empRequest = convertToDto(employeeInput, CreateEmployeeRequest.class);
        return this.employeeService.createEmployee(empRequest);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {

        return this.employeeService.deleteEmployeeById(id);
    }

    private <T> T convertToDto(Object input, Class<T> clazz) {
        return this.objectMapper.convertValue(input, clazz);
    }
}
