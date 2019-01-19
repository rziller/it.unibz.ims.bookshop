package it.unibz.ims.bookshop.controller;

import it.unibz.ims.bookshop.models.Product;
import it.unibz.ims.bookshop.services.MultiValueMapService;
import it.unibz.ims.bookshop.services.ProductService;
import it.unibz.ims.bookshop.services.ViewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ViewDataService<Product> viewDataService;

    @GetMapping("/admin")
    String getHome(@RequestParam Map<String, String> queryParameters,
                   HttpServletRequest request,
                   HttpSession session,
                   Model model) {
        Page<Product> products = productService.findAll(queryParameters, request);
        model.addAttribute("viewData", viewDataService.createWithPagination(products, request, queryParameters) );
        return "AdminView";
    }

    @GetMapping("/admin/product/{id}")
    String getProductDetails(@PathVariable("id") String id, HttpServletRequest request, HttpSession session, Model model) {
        Optional<Product> product = productService.findById(id);
        model.addAttribute("viewData", viewDataService.createWithoutPagination(product) );
        return "ProductDetailsAdminView";
    }

    @PostMapping(
            value = "/admin/product/delete",
            consumes = "application/x-www-form-urlencoded"
    )
    String deleteProduct(
            @RequestParam Map<String, String> queryParameters,
            @RequestBody MultiValueMap<String, String> requestBody,
            HttpServletRequest request,
            HttpSession session,
            Model model) {
        Optional<String> productId = MultiValueMapService.<String, String>getFirstParamaterValue(requestBody, "productId");

        if ( !productId.isPresent() ) {
            return "ErrorView";
        }

        boolean deleteResult = productService.deleteById( UUID.fromString(productId.get()) );

        if (!deleteResult) {
            return "ErrorView";
        }

        Page<Product> products = productService.findAll(queryParameters, request);
        model.addAttribute("viewData", viewDataService.createWithPagination(products, request, queryParameters) );
        return "AdminView";
    }
}
