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
public class UserProfileController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ViewDataService<Country> viewDataServiceCountry;

    @Autowired
    private ViewDataService<Customer> viewDataServiceCustomer;

    @Autowired
    private ViewDataService<Address> viewDataServiceAddress;

    @Autowired
    private ViewDataService<List<Order>> viewDataServiceCustomerOrders;

    @GetMapping("/userprofile")
    String getUserProfileView(Authentication auth, HttpServletRequest request, HttpSession session, Model model) {
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

        return "UserProfileView";
    }

    @GetMapping("/userprofile/orders")
    String getUserProfileOrdersView(Authentication auth, Model model) {
        Optional<List<Order>> customerOrdersOptional = userProfileService.findUserOrders(auth);
        model.addAttribute("viewData", viewDataServiceCustomerOrders.createWithoutPagination(customerOrdersOptional));
        return "UserProfileOrdersView";
    }

    @PostMapping(
            value = "/userprofile",
            consumes = "application/x-www-form-urlencoded"
    )
    String userprofile(
            Authentication auth,
            HttpServletRequest request,
            HttpSession session,
            Model model,
            @RequestBody MultiValueMap<String, String> requestBody
    ) {
        List<Country> countries = countryService.findAll();
        Optional<Customer> customerOptional = userProfileService.updateUserProfile(auth,requestBody);
        Optional<Order> shoppingCart = shoppingCartService.findBySession(session);
        model.addAttribute("viewDataCountries", viewDataServiceCountry.createWithoutPagination(countries));

        model.addAttribute("viewDataCustomer", viewDataServiceCustomer.createWithoutPagination(customerOptional));

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            model.addAttribute("viewDataBillingAddress",
                    viewDataServiceAddress.createWithoutPagination(customer.getBillingAddress()));
            model.addAttribute("viewDataShippingAddress",
                    viewDataServiceAddress.createWithoutPagination(customer.getShippingAddress()));
        }

        return "UserProfileView";
    }
}
