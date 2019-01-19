package it.unibz.ims.bookshop.controller;

import it.unibz.ims.bookshop.models.Product;
import it.unibz.ims.bookshop.services.ProductService;
import it.unibz.ims.bookshop.services.ViewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

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
}
