package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.store.ProductCategory;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.ProductState;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.entity.Product;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return productRepository.findAllByProductCategory(category, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product entity = mapper.toEntity(productDto);
        Product saved = productRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(404, "Product id=" + productDto.getProductId() + "not found"));
        mapper.update(product, productDto);
        productRepository.saveAndFlush(product);
        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(404, "Product id=" + productId + "not found"));
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateQuantity(SetProductQuantityStateRequest updateQuantityDto) {
        Product product = productRepository.findById(updateQuantityDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(404, "Product id=" + updateQuantityDto.getProductId() + "not found"));
        product.setQuantityState(updateQuantityDto.getQuantityState());
        productRepository.save(product);
        return mapper.toDto(product);
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(404, "Product id=" + productId + "not found"));
        return mapper.toDto(product);
    }
}
