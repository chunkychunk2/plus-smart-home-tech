package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.entity.DeliveryRepository;
import ru.yandex.practicum.mapper.DeliveryMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class DeliveryServiceImplTest {

    @Autowired
    private DeliveryServiceImpl service;

    @MockBean
    private DeliveryMapper deliveryMapper;
    @MockBean
    private DeliveryRepository deliveryRepository;
    @MockBean
    private OrderClient orderClient;
    @MockBean
    private WarehouseClient warehouseClient;

    @Test
    void calculateDelivery() {
        double expected = 27.6;
        DeliveryDto dto = DeliveryDto.builder()
                .fromAddress(AddressDto.builder()
                        .flat("ADDRESS_2")
                        .house("ADDRESS_2")
                        .street("ADDRESS_2")
                        .city("ADDRESS_2")
                        .country("ADDRESS_2")
                        .build())
                .toAddress(AddressDto.builder()
                        .street("Пролетарская")
                        .house("31")
                        .build())
                .deliveryWeight(10.)
                .deliveryVolume(10.)
                .fragile(true)
                .build();


        Double calculated = service.calculateDelivery(dto);

        assertEquals(expected, calculated);
    }
}