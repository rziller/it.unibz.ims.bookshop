package it.unibz.ims.bookshop.controller;

import it.unibz.ims.bookshop.models.Product;
import it.unibz.ims.bookshop.services.ProductService;
import it.unibz.ims.bookshop.services.ViewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ViewDataService<Product> viewDataService;

    @GetMapping("/product/{id}")
    String getProductDetails(@PathVariable("id") String id, HttpServletRequest request, HttpSession session, Model model) {
        Optional<Product> product = productService.findById(id);
        model.addAttribute("viewData", viewDataService.createWithoutPagination(product) );
        return "ProductDetailsView";
    }
}
