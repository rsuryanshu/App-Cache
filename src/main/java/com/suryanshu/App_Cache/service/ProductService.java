package com.suryanshu.App_Cache.service;

import com.suryanshu.App_Cache.dao.ProductRepository;
import com.suryanshu.App_Cache.dto.ProductDTO;
import com.suryanshu.App_Cache.dto.ProductPageDTO;
import com.suryanshu.App_Cache.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @CacheEvict(value = "products-all", allEntries = true)
    public ProductDTO saveProduct(Product product) {
        Product saved = productRepository.save(product);
        return new ProductDTO(saved);
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProduct(Long id) {
        System.out.println(">>> CACHE MISS — hitting database for id: " + id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductDTO(product);
    }

    @Cacheable(value = "products-all", key = "#pageable.pageNumber + '-' + #pageable.pageSize", sync = true)
    public ProductPageDTO getAllProducts(Pageable pageable) {
        System.out.println(">>> CACHE MISS — hitting database ");
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDTO> dtoList = productPage.getContent()
                .stream()
                .map(ProductDTO::new)
                .toList();

        return new ProductPageDTO(
                dtoList,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    @Caching(
            put = {@CachePut(value = "products", key = "#product.id")},
            evict = {@CacheEvict(value = "products-all", allEntries = true)}
    )
    public ProductDTO updateProduct(Product product) {
        Product saved = productRepository.save(product);
        return new ProductDTO(saved);
    }

    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "products-all", allEntries = true)
    })
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
