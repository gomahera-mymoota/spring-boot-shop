package com.kosa.shop.controller;

import com.kosa.shop.dto.OrderDto;
import com.kosa.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                                BindingResult bindingResult,
                                Principal principal) {
        if (bindingResult.hasErrors()) {
            var sb = new StringBuilder();
            var fiedlErrors = bindingResult.getFieldErrors();

            for (var fieldEror : fiedlErrors) {
                sb.append(fieldEror.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        var email = principal.getName();
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping({"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        var pageable = PageRequest.of(page.orElse(0), 4);
        var ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
