package it.unibz.ims.bookshop.controller;

import it.unibz.ims.bookshop.models.Product;
import it.unibz.ims.bookshop.services.ProductService;
import it.unibz.ims.bookshop.services.ViewDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class SocialLoginController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ViewDataService<Product> viewDataService;

    @GetMapping("/oauthlogin")
    String getSocialLoginView(HttpServletRequest request, HttpSession session, Model model) {
        return "SocialLoginView";
    }

}
