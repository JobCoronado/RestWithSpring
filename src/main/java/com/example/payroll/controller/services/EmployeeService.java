package com.example.payroll.controller.services;

import com.example.payroll.controller.repository.EmployeeRepository;
import com.example.payroll.exception.EmployeeNotFoundException;
import com.example.payroll.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    public Employee getEmployeeById(Long idEmployee){
        return this.repository.findById(idEmployee)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(idEmployee)
                );
    }

    public Employee createEmployee(Employee newEmployee){
        return this.repository.save(newEmployee);
    }

    public Employee updateEmployee(Employee newEmployee, Long idEmployee){
        return this.repository.findById(idEmployee)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(idEmployee);
                    return repository.save(newEmployee);
                });
    }

    public void deleteEmployee(Long idEmployee){
        this.repository.deleteById(idEmployee);
    }








}
