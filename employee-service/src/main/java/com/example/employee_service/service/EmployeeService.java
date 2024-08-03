package com.example.employee_service.service;

import com.example.employee_service.dto.APIResponseDto;
import com.example.employee_service.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);

    APIResponseDto getEmployeeById(Long employeeId);
}