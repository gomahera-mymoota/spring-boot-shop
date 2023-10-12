package com.kosa.shop.controller;

import com.kosa.shop.dto.ItemSearchDto;
import com.kosa.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.print.Pageable;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        var pageable = PageRequest.of(page.orElse(0), 6);
        var items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }
}
