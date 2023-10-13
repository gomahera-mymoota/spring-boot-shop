package com.kosa.shop.controller;

import com.kosa.shop.dto.OrderDto;
import com.kosa.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;

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
}
