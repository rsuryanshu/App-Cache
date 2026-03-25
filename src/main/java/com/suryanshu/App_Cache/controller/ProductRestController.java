package com.suryanshu.App_Cache.controller;

import com.suryanshu.App_Cache.dto.ProductDTO;
import com.suryanshu.App_Cache.dto.ProductPageDTO;
import com.suryanshu.App_Cache.entity.Product;
import com.suryanshu.App_Cache.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    @Autowired
    ProductService productService;

    @PostMapping("/product")
    public ProductDTO saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping("/product/{id}")
    ProductDTO getProducts(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        ProductDTO p = productService.getProduct(id);
        long time = System.currentTimeMillis() - start;
        System.out.println("Time taken: " + time + "ms");
        return p;
    }

    @GetMapping("/product")
    public ProductPageDTO getAllProducts(Pageable pageable) {
        long start = System.currentTimeMillis();
        ProductPageDTO allProducts = productService.getAllProducts(pageable);
        long time = System.currentTimeMillis() - start;
        System.out.println("Time taken: " + time + "ms");
        return allProducts;
    }

    @PutMapping("/product")
    public ProductDTO updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Deleted product with id: " + id;
    }
}
