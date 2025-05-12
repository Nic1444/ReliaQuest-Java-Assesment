package com.reliaquest.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.client.EmployeeMockServiceClient;
import com.reliaquest.api.dto.request.CreateEmployeeRequest;
import com.reliaquest.api.dto.response.EmployeeDto;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeMockServiceClient employeeMockServiceClient;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testGetAllEmployees() {
        List<EmployeeDto> mockEmployees = List.of(
                new EmployeeDto("1", "John", 5000, 30, "Engineer", "Test@gmail.com"),
                new EmployeeDto("2", "Cena", 55000, 30, "Engineer 2", "Test@gmail.com"));
        when(employeeMockServiceClient.getAllEmployees()).thenReturn(ResponseEntity.ok(mockEmployees));

        var response = employeeService.getAllEmployees();

        assertEquals(2, response.getBody().size());
        verify(employeeMockServiceClient).getAllEmployees();
    }

    @Test
    void testGetEmployeesByName() {
        List<EmployeeDto> mockEmployees = List.of(new EmployeeDto("1", "John", 5000, 30, "Engineer", "Test@gmail.com"));
        when(employeeMockServiceClient.getEmployeesByName("John")).thenReturn(ResponseEntity.ok(mockEmployees));

        var response = employeeService.getEmployeesByName("John");

        assertEquals("John", response.getBody().get(0).getEmployeeName());
        verify(employeeMockServiceClient).getEmployeesByName("John");
    }

    @Test
    void testGetEmployeeById() {
        EmployeeDto dto = new EmployeeDto("1", "Jane", 6000, 28, "Analyst", "Test@gmail.com");
        when(employeeMockServiceClient.getEmployeeById("1")).thenReturn(ResponseEntity.ok(dto));

        var response = employeeService.getEmployeeById("1");

        assertEquals("Jane", response.getBody().getEmployeeName());
        verify(employeeMockServiceClient).getEmployeeById("1");
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        when(employeeMockServiceClient.getHighestSalaryOfEmployees()).thenReturn(ResponseEntity.ok(10000));

        var response = employeeService.getHighestSalaryOfEmployees();

        assertEquals(10000, response.getBody());
        verify(employeeMockServiceClient).getHighestSalaryOfEmployees();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<String> names = List.of("Alice", "Bob");
        when(employeeMockServiceClient.getTopTenHighestEarningEmployeeNames()).thenReturn(ResponseEntity.ok(names));

        var response = employeeService.getTopTenHighestEarningEmployeeNames();

        assertTrue(response.getBody().contains("Alice"));
        verify(employeeMockServiceClient).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void testCreateEmployee() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmployeeName("David");
        request.setEmployeeSalary(7500);
        request.setEmployeeAge(35);
        request.setEmployeeTitle("Manager");

        EmployeeDto dto = new EmployeeDto("2", "David", 7500, 35, "Manager", "Test@gmail.com");
        when(employeeMockServiceClient.createEmployee(request)).thenReturn(ResponseEntity.ok(dto));

        var response = employeeService.createEmployee(request);

        assertEquals("David", response.getBody().getEmployeeName());
        verify(employeeMockServiceClient).createEmployee(request);
    }

    @Test
    void testDeleteEmployeeById() {
        when(employeeMockServiceClient.deleteEmployeeById("123")).thenReturn(ResponseEntity.ok("Deleted"));

        var response = employeeService.deleteEmployeeById("123");

        assertEquals("Deleted", response.getBody());
        verify(employeeMockServiceClient).deleteEmployeeById("123");
    }
}
