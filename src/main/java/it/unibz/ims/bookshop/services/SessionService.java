package it.unibz.ims.bookshop.services;

import it.unibz.ims.bookshop.Constants;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class SessionService {

    public String getUserId(HttpSession session) {
        return (String) session.getAttribute(Constants.USER_ID_PARAM_NAME);
    }

    public UUID getShoppingCartId(HttpSession session) {
        return (UUID) session.getAttribute(Constants.SHOPPING_CART_ID_PARAM_NAME);
    }

    public void setShoppingCartId(HttpSession session, UUID shoppingCartId) {
        session.setAttribute(Constants.SHOPPING_CART_ID_PARAM_NAME, shoppingCartId);
    }

    public void removeShoppingCartId(HttpSession session) {
        session.removeAttribute(Constants.SHOPPING_CART_ID_PARAM_NAME);
    }

    public void incrementShoppingCartTotalByOne(HttpSession session) {
        int currentTotal = this.getCurrentShoppingCartTotal(session);
        int newTotal = (currentTotal <= 0) ? 1 : currentTotal + 1;
        session.setAttribute(Constants.SHOPPING_CART_TOTAL_PRODUCTS, newTotal);
    }

    public void decrementShoppingCartTotalByOne(HttpSession session) {
        int currentTotal = this.getCurrentShoppingCartTotal(session);
        int newTotal = (currentTotal <= 0) ? 0 : currentTotal - 1;
        session.setAttribute(Constants.SHOPPING_CART_TOTAL_PRODUCTS, newTotal);
    }

    public void setShoppingCartTotalToZero(HttpSession session) {
        session.setAttribute(Constants.SHOPPING_CART_TOTAL_PRODUCTS, 0);
    }

    public void removeShoppingCartTotal(HttpSession session) {
        session.removeAttribute(Constants.SHOPPING_CART_TOTAL_PRODUCTS);
    }

    private int getCurrentShoppingCartTotal(HttpSession session) {
        return (Integer) session.getAttribute(Constants.SHOPPING_CART_TOTAL_PRODUCTS);
    }
}
