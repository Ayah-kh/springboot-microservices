package com.example.employee_service.service.impl;

import com.example.employee_service.dto.APIResponseDto;
import com.example.employee_service.dto.DepartmentDto;
import com.example.employee_service.dto.EmployeeDto;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.mapper.EmployeeMapper;
import com.example.employee_service.repo.EmployeeRepository;
import com.example.employee_service.service.ApiClient;
import com.example.employee_service.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRepository employeeRepository;
    private ApiClient apiClient;
    private WebClient webClient;
   // private RestTemplate restTemplate;
//    private WebClient webClient;
//    private APIClient apiClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        Employee saveDEmployee = employeeRepository.save(employee);

        EmployeeDto savedEmployeeDto = EmployeeMapper.mapToEmployeeDto(saveDEmployee);

        return savedEmployeeDto;
    }

    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();

//        ResponseEntity<DepartmentDto> response = restTemplate.getForEntity("http://localhost:8080/api/departments/" + employee.getDepartmentCode(),
//                DepartmentDto.class);
//        DepartmentDto departmentDto = response.getBody();

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();
//        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());
        EmployeeDto employeeDto = new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentCode(),
                employee.getOrganizationCode()
        );

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }

    public APIResponseDto getDefaultDepartment(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("R&D");
        departmentDto.setDepartmentCode("RD001");
        departmentDto.setDepartmentDescription("Research and development");
        EmployeeDto employeeDto = new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentCode(),
                employee.getOrganizationCode()
        );

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }


    //@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
//    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
//    @Override
//    public APIResponseDto getEmployeeById(Long employeeId) {
//
//        LOGGER.info("inside getEmployeeById() method");
//        Employee employee = employeeRepository.findById(employeeId).get();
//
////        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://DEPARTMENT-SERVICE/api/departments/" + employee.getDepartmentCode(),
////                DepartmentDto.class);
////
////        DepartmentDto departmentDto = responseEntity.getBody();
//
//        DepartmentDto departmentDto = webClient.get()
//                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
//                .retrieve()
//                .bodyToMono(DepartmentDto.class)
//                .block();
//
//      //  DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());
//
//        OrganizationDto organizationDto = webClient.get()
//                .uri("http://localhost:8083/api/organizations/" + employee.getOrganizationCode())
//                .retrieve()
//                .bodyToMono(OrganizationDto.class)
//                .block();
//
//        EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employee);
//
//        APIResponseDto apiResponseDto = new APIResponseDto();
//        apiResponseDto.setEmployee(employeeDto);
//        apiResponseDto.setDepartment(departmentDto);
//        apiResponseDto.setOrganization(organizationDto);
//        return apiResponseDto;
//    }
//
//    public APIResponseDto getDefaultDepartment(Long employeeId, Exception exception) {
//
//        LOGGER.info("inside getDefaultDepartment() method");
//
//        Employee employee = employeeRepository.findById(employeeId).get();
//
//        DepartmentDto departmentDto = new DepartmentDto();
//        departmentDto.setDepartmentName("R&D Department");
//        departmentDto.setDepartmentCode("RD001");
//        departmentDto.setDepartmentDescription("Research and Development Department");
//
//        EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employee);
//
//        APIResponseDto apiResponseDto = new APIResponseDto();
//        apiResponseDto.setEmployee(employeeDto);
//        apiResponseDto.setDepartment(departmentDto);
//        return apiResponseDto;
//    }
}