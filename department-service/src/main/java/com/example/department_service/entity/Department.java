package com.example.department_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Specify the column name
    private Long id;

    @Column(name = "department_name")  // Specify the column name
    private String departmentName;

    @Column(name = "department_description")  // Specify the column name
    private String departmentDescription;

    @Column(name = "department_code")  // Specify the column name
    private String departmentCode;
}
