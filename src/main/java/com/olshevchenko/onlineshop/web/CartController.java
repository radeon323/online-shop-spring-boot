package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.entity.CartItem;
import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.service.CartService;
import com.olshevchenko.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@Controller
@RequestMapping()
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private List<CartItem> cartItems = new CopyOnWriteArrayList<>();

    @GetMapping("/cart")
    protected String getCart(HttpSession session, ModelMap model) {
        createCartAndReturnListOfCartItems(session, model);
        return "cart";
    }

    @PostMapping("/products")
    protected String addProductToCart(@RequestParam int id, HttpSession session, ModelMap model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        cartService.addToCart(cartItems,id);
        session.setAttribute("cartItems", cartItems);

        return "products_list";
    }

    @GetMapping("/cart/delete")
    protected String removeProductFromCart(@RequestParam int id, HttpSession session, ModelMap model) {
        List<CartItem> cartItems = createCartAndReturnListOfCartItems(session, model);
        cartService.removeFromCart(cartItems, id);
        session.setAttribute("cartItems", cartItems);
        createCartAndReturnListOfCartItems(session, model);
        return "cart";
    }

    @PostMapping("/cart/update")
    protected String updateCart(@RequestParam int id, @RequestParam(defaultValue = "0") int quantity, HttpSession session, ModelMap model) {
        List<CartItem> cartItems = createCartAndReturnListOfCartItems(session, model);
        if (quantity == 0) {
            String errorMsg = "Please specify quantity!";
            model.addAttribute("errorMsg", errorMsg);
            return "cart";
        }
        cartService.updateQuantity(cartItems,id,quantity);
        session.setAttribute("cartItems", cartItems);
        createCartAndReturnListOfCartItems(session, model);
        return "cart";
    }

    @SuppressWarnings("unchecked")
    List<CartItem> createCartAndReturnListOfCartItems(HttpSession session, ModelMap model) {
        cartItems = (List<CartItem>) session.getAttribute("cartItems");

        model.addAttribute("cartItems", cartItems);
        double totalPrice = cartService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return cartItems;
    }


}
