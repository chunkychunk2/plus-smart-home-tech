package ru.practicum.telemetry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.telemetry.dto.hub.HubEvent;
import ru.practicum.telemetry.dto.sensor.SensorEvent;
import ru.practicum.telemetry.service.CollectorService;

@Slf4j
@RestController
@RequestMapping("/events/")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService service;

    @PostMapping("/sensors")
    public void sensorEvent(@RequestBody @Valid SensorEvent sensorEvent) {
        log.info("getting sensor event {}", sensorEvent);
        service.sendSensorEvent(sensorEvent);
    }

    @PostMapping("/hubs")
    public void hubEvent(@RequestBody @Valid HubEvent hubEvent) {
        log.info("getting hub event {}", hubEvent);
        service.sendHubEvent(hubEvent);
    }
}