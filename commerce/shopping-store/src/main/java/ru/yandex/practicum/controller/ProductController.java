package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.store.ProductCategory;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController implements ShoppingStoreClient {

    private final ProductService productService;

    @Override
    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("Getting products by params: category={}, pageable={}", category, pageable);
        return productService.getProducts(category, pageable);
    }

    @Override
    @PutMapping
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Adding product={}", productDto);
        return productService.addProduct(productDto);
    }

    @Override
    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Updating product id={}", productDto.getProductId());
        return productService.updateProduct(productDto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    public ProductDto deleteProduct(@RequestBody UUID id) {
        log.info("Deleting product id={}", id);
        return productService.deleteProduct(id);
    }

    @Override
    @PostMapping("/quantityState")
    public ProductDto updateQuantityState(@Valid SetProductQuantityStateRequest request) {
        log.info("Updating quantity product id={}, quantity state{}", request.getProductId(), request.getQuantityState());
        return productService.updateQuantity(request);
    }

    @Override
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable UUID productId) {
        log.info("Getting product by id={}", productId);
        return productService.getProductById(productId);
    }

}
