package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {

    private final WarehouseService warehouseService;

    @Override
    @PutMapping
    public void addNewProductToWareHouse(@RequestBody NewProductInWarehouseRequest request) {
        log.info("Adding new product to warehouse, request={}", request);
        warehouseService.addNewProductToWarehouse(request);
    }

    @Override
    @PostMapping("/check")
    public BookedProductsDto checkProducts(@RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("Checking product quantity for cart, cart={}", shoppingCartDto);
        return warehouseService.checkWarehouse(shoppingCartDto);
    }

    @Override
    @PostMapping("/add")
    public void addProductsToWareHouse(@RequestBody AddProductToWarehouseRequest request) {
        log.info("Getting request to add product to warehouse, request={}", request);
        warehouseService.addProductToWarehouse(request);
    }

    @Override
    public AddressDto getAddress() {
        log.info("Getting random request");
        return warehouseService.getAddress();
    }

    @Override
    public void shipProductsToDelivery(ShippedToDeliveryRequest request) {
        log.info("Shipping products: {}", request);
        warehouseService.shipProducts(request);
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products) {
        log.info("Returning products={}", products);
        warehouseService.returnProducts(products);
    }

    @Override
    public BookedProductsDto assembleOrder(AssemblyProductsForOrderRequest request) {
        log.info("Assemble order, request={}", request);
        return warehouseService.assembleOrder(request);
    }
}
