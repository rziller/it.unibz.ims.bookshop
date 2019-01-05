package it.unibz.ims.bookshop.controller;

import it.unibz.ims.bookshop.models.Order;
import it.unibz.ims.bookshop.services.ShoppingCartService;
import it.unibz.ims.bookshop.services.ViewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ViewDataService<Order> viewDataService;


    @GetMapping("/shoppingcart")
    String getShoppingCart(HttpServletRequest request, HttpSession session, Model model) {
        Optional<Order> shoppingCart = shoppingCartService.findBySession(session);
        model.addAttribute("viewData",  viewDataService.createWithoutPagination(shoppingCart));
        return "ShoppingCartView";
    }

    @PostMapping(
            value = "/shoppingcart/product",
            consumes = "application/x-www-form-urlencoded"
    )
    String addProduct(
            @RequestBody MultiValueMap<String, String> requestBody,
            HttpServletRequest request,
            HttpSession session,
            HttpServletResponse response,
            Model model) {
        Optional<Order> shoppingCart = shoppingCartService.addProduct(session, requestBody);
        //model.addAttribute("viewData", viewDataService.createWithoutPagination(shoppingCart));
        //return "ShoppingCartView";
        return "redirect:/";
    }

    @PostMapping(
            value = "/shoppingcart/product/edit",
            consumes = "application/x-www-form-urlencoded"
    )
    String updateProduct(
            @RequestBody MultiValueMap<String, String> requestBody,
            HttpServletRequest request,
            HttpSession session,
            Model model) {
        Optional<Order> shoppingCart = shoppingCartService.updateProduct(session, requestBody);
        model.addAttribute("viewData", viewDataService.createWithoutPagination(shoppingCart));
        return "ShoppingCartView";
    }

    @PostMapping(
            value = "/shoppingcart/product/delete",
            consumes = "application/x-www-form-urlencoded"
    )
    String deleteProduct(
            @RequestBody MultiValueMap<String, String> requestBody,
            HttpServletRequest request,
            HttpSession session,
            Model model) {
        Optional<Order> shoppingCart = shoppingCartService.deleteProduct(session, requestBody);
        model.addAttribute("viewData", viewDataService.createWithoutPagination(shoppingCart));
        return "ShoppingCartView";
    }

    @PostMapping(
            value = "/shoppingcart/product-list/delete",
            consumes = "application/x-www-form-urlencoded"
    )
    String deleteAllItemsInShoppingCart(
            @RequestBody MultiValueMap<String, String> requestBody,
            HttpServletRequest request,
            HttpSession session,
            Model model) {
        Optional<Order> shoppingCart = shoppingCartService.deleteAllProducts(session, requestBody);
        model.addAttribute("viewData", viewDataService.createWithoutPagination(shoppingCart) );
        return "ShoppingCartView";
    }
}
