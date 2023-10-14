package com.kosa.shop.controller;

import com.kosa.shop.domain.entity.id.CartItemId;
import com.kosa.shop.dto.CartItemDto;
import com.kosa.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    @ResponseBody
    public ResponseEntity<?> order(@RequestBody @Valid CartItemDto cartItemDto,
                                   BindingResult bindingResult,
                                   Principal principal) {
        if (bindingResult.hasErrors()) {
            var sb = new StringBuilder();
            var fieldErrors = bindingResult.getFieldErrors();

            // for 문을 Java Streams로
            fieldErrors.stream()
                    .map(FieldError::getDefaultMessage)
                    .forEach(sb::append);

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        var email = principal.getName();

        try {
            var cartItemId = cartService.addCartItem(cartItemDto, email);

            // 교재와 달리 try 문 안에서 return
            return new ResponseEntity<CartItemId>(cartItemId, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cart")
    public String cartDetailList(Principal principal, Model model) {
        var cartDetailList = cartService.getCartItemList(principal.getName());

        model.addAttribute("cartItems", cartDetailList);

        return "cart/cartList";
    }
}
