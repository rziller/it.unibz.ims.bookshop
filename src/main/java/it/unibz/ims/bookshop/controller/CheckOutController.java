package it.unibz.ims.bookshop.controller;

import it.unibz.ims.bookshop.models.*;
import it.unibz.ims.bookshop.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckOutController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ViewDataService<Country> viewDataServiceCountry;

    @Autowired
    private ViewDataService<CheckoutResult> viewDataServiceCheckoutResult;

    @Autowired
    private ViewDataService<Customer> viewDataServiceCustomer;

    @Autowired
    private ViewDataService<Address> viewDataServiceAddress;

    @GetMapping("/checkout")
    String getCheckOutView(Authentication auth, HttpServletRequest request, HttpSession session, Model model) {
        List<Country> countries = countryService.findAll();
        Optional<Customer> authCustomerOptional = userProfileService.findUserProfile(auth);
        model.addAttribute("viewDataCountries", viewDataServiceCountry.createWithoutPagination(countries));
        model.addAttribute("viewDataCustomer", viewDataServiceCustomer.createWithoutPagination(authCustomerOptional));

        if (authCustomerOptional.isPresent()) {
            Customer authCustomer = authCustomerOptional.get();
            model.addAttribute("viewDataBillingAddress",
                    viewDataServiceAddress.createWithoutPagination(authCustomer.getBillingAddress()));
            model.addAttribute("viewDataShippingAddress",
                    viewDataServiceAddress.createWithoutPagination(authCustomer.getShippingAddress()));
        }

        return "CheckOutView";
    }

    @PostMapping(
            value = "/checkout/result",
            consumes = "application/x-www-form-urlencoded"
    )
    String checkout(
            Authentication auth,
            HttpServletRequest request,
            HttpSession session,
            Model model,
            @RequestBody MultiValueMap<String, String> requestBody
            ) {
        Optional<Customer> customer = userProfileService.findUserProfile(auth);
        Optional<Order> shoppingCart = shoppingCartService.findBySession(session);
        CheckoutResult result = checkoutService.checkout(customer, shoppingCart, session, requestBody);
        model.addAttribute("viewData", viewDataServiceCheckoutResult.createWithoutPagination(result));
        return "CheckoutResultView";
    }
}
