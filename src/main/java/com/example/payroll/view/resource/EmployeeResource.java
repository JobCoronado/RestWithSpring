package com.example.payroll.view.resource;

import com.example.payroll.controller.services.EmployeeService;
import com.example.payroll.model.Employee;
import com.example.payroll.view.resource.assembler.EmployeeModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeResource {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeModelAssembler;

    public EmployeeResource(EmployeeService employeeService, EmployeeModelAssembler employeeModelAssembler) {
        this.employeeService = employeeService;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = employeeService.getEmployees().stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeResource.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    public ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> employee = employeeModelAssembler.toModel(employeeService.createEmployee(newEmployee));
        return ResponseEntity
                .created(employee.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(employee);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new EntityModel<>(
                employee,
                linkTo(methodOn(EmployeeResource.class).one(id)).withSelfRel(),
                linkTo(methodOn(EmployeeResource.class).all()).withRel("employees"));
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        Employee employeeUpdated = employeeService.updateEmployee(newEmployee, id);

        EntityModel<Employee> employeeEntityModel = employeeModelAssembler.toModel(employeeUpdated);

        return ResponseEntity.created(employeeEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(employeeEntityModel);

    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

}
