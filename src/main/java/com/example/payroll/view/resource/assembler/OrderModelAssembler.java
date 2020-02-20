package com.example.payroll.view.resource.assembler;

import com.example.payroll.model.Employee;
import com.example.payroll.model.Order;
import com.example.payroll.model.Status;
import com.example.payroll.view.resource.EmployeeResource;
import com.example.payroll.view.resource.OrderResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {


    @Override
    public EntityModel<Order> toModel (Order order) {
        EntityModel orderModel = new EntityModel<>(order,
                linkTo(methodOn(EmployeeResource.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeResource.class).all()).withRel("order"));

        // Conditional links based on state of the order

        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(
                    linkTo(methodOn(OrderResource.class)
                            .cancel(order.getId())).withRel("cancel"));
            orderModel.add(
                    linkTo(methodOn(OrderResource.class)
                            .complete(order.getId())).withRel("complete"));
        }
        return orderModel;
    }
}
