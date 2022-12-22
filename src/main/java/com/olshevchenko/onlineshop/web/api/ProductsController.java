package com.olshevchenko.onlineshop.web.api;

import com.olshevchenko.onlineshop.dto.ProductDto;
import com.olshevchenko.onlineshop.entity.Product;
import com.olshevchenko.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author Oleksandr Shevchenko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/products/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductsController {

    private final ProductService productService;

    @GetMapping()
    protected List<Product> showAllProducts(@RequestParam(value = "sort", required = false) String sort) {

        List<Product> products;

        if (sort != null && Objects.equals(sort, "price")) {
            products = productService.sortByPrice();
        } else if (sort != null && Objects.equals(sort, "name")) {
            products = productService.sortByName();
        } else {
            products = productService.findAll();
        }
        return products;
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") int id) {
        Product product = productService.findById(id);
        ProductDto productDto = entityToDto(product);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping()
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = dtoToEntity(productDto);
        productService.save(product);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping("{id}")
    protected ResponseEntity<ProductDto> editProduct(@PathVariable("id") int id, @Valid @RequestBody ProductDto productDto) {
        Product product = dtoToEntity(productDto);
        product.setId(id);
        productService.save(product);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("{id}")
    protected void deleteProduct(@PathVariable int id) {
        productService.delete(id);
    }


    private Product dtoToEntity(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build();
    }

    private ProductDto entityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .creationDate(product.getCreationDate())
                .build();
    }



}
