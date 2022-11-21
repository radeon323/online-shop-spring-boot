package com.olshevchenko.onlineshop.web;

import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductsController {

    private final ProductService productService;

    @GetMapping()
    protected String showAllProducts(ModelMap model) {
        createProductsList(model);
        return "products_list";
    }

    @GetMapping("/add")
    protected String getAddProductPage(ModelMap model) {
        model.remove("msgSuccess");
        return "add_product";
    }

    @PostMapping("/add")
    protected String addProduct(@RequestParam(defaultValue = "0") int id,
                      @RequestParam String name,
                      @RequestParam(defaultValue = "0") double price,
                      @RequestParam(defaultValue = "") String description,
                      ModelMap model) {

        Optional<Product> optionalProduct = validateAndGetProduct(id, name, price, description, model);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("product", product);
            productService.add(product);
            String msgSuccess = String.format("Product <i>%s</i> was successfully added!", product.getName());
            model.addAttribute("msgSuccess", msgSuccess);
        }
        return "add_product";
    }

    @GetMapping("/edit")
    protected String getEditProductPage(HttpServletRequest request,
                              @RequestParam int id,
                              ModelMap model) {
        Optional<Product> optionalProduct = productService.findById(id);
        optionalProduct.ifPresent(product -> model.addAttribute("product", product));

        return "edit_product";
    }

    @PostMapping("/edit")
    protected String editProduct(@RequestParam int id,
                       @RequestParam String name,
                       @RequestParam(defaultValue = "0") double price,
                       @RequestParam(defaultValue = "") String description,
                       ModelMap model) {

        Optional<Product> optionalProduct = validateAndGetProduct(id, name, price, description, model);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("product", product);
            productService.edit(product);
            String msgSuccess = String.format("Product <i>%s</i> was successfully changed!", product.getName());
            model.addAttribute("msgSuccess", msgSuccess);
        }
        return "edit_product";
    }

    @GetMapping("/delete")
    protected String deleteProduct(@RequestParam int id, ModelMap model) {
        productService.remove(id);
        String msgSuccess = String.format("Product with id:%d was successfully deleted!", id);
        model.addAttribute("msgSuccess", msgSuccess);

        createProductsList(model);
        return "products_list";
    }


    void createProductsList(ModelMap model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
    }


    Optional<Product> validateAndGetProduct(int id, String name, double price, String description, ModelMap model) {
        Product product = Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
        if (!name.isEmpty() && price != 0) {
            return Optional.of(product);
        } else {
            String errorMsg = "Please fill up all necessary fields!";
            model.addAttribute("errorMsg", errorMsg);
            model.addAttribute("product", product);
            return Optional.empty();
        }
    }


}
