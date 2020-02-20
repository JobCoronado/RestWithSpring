package com.example.payroll.view.resource.assembler;

import com.example.payroll.model.Employee;
import com.example.payroll.view.resource.EmployeeResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        return new EntityModel<>(employee,
                linkTo(methodOn(EmployeeResource.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeResource.class).all()).withRel("employees"));
    }
}
