package com.example.payroll.view.resource;

import com.example.payroll.controller.services.OrderService;
import com.example.payroll.exception.OrderNotFoundException;
import com.example.payroll.model.Employee;
import com.example.payroll.model.Order;

import com.example.payroll.model.Status;
import com.example.payroll.view.resource.assembler.OrderModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderResource {
    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;

    public OrderResource(OrderService orderService, OrderModelAssembler orderModelAssembler) {
        this.orderService = orderService;
        this.orderModelAssembler = orderModelAssembler;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> Orders = orderService.getOrders().stream()
                .map(orderModelAssembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(Orders,
                linkTo(methodOn(OrderResource.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return new EntityModel<>(
                order,
                linkTo(methodOn(OrderResource.class).one(id)).withSelfRel(),
                linkTo(methodOn(OrderResource.class).all()).withRel("order"));
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order newOrder) {

        newOrder.setStatus(Status.IN_PROGRESS);
        EntityModel<Order> order = orderModelAssembler.toModel(orderService.createOrder(newOrder));
        return ResponseEntity
                .created(linkTo(methodOn(OrderResource.class).one(newOrder.getId())).toUri())
                .body(order);
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<RepresentationModel> cancel(@PathVariable Long id) {

        Order order = orderService.getOrderById(id);

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(orderModelAssembler.toModel(orderService.updateOrder(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<RepresentationModel> complete(@PathVariable Long id) {

        Order order = orderService.getOrderById(id);

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(orderModelAssembler.toModel(orderService.updateOrder(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
    }
}
