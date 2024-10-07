package org.development.testmigrations;

import org.development.testmigrations.entity.LaptopEntity;
import org.development.testmigrations.entity.StoreEntity;
import org.development.testmigrations.repo.LaptopRepository;
import org.development.testmigrations.repo.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
public class Controller {

    private final LaptopRepository laptopRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public Controller(LaptopRepository laptopRepository, StoreRepository storeRepository) {
        this.laptopRepository = laptopRepository;
        this.storeRepository = storeRepository;
    }


    @GetMapping("/create-store")
    public ResponseEntity<?> createStore() {
        StoreEntity storeEntity = new StoreEntity(UUID.randomUUID(), "Store " + UUID.randomUUID(), "Test", LocalDateTime.now());
        return new ResponseEntity<>(storeRepository.save(storeEntity), HttpStatus.OK);
    }

    @GetMapping("/create-laptop/{id}")
    public ResponseEntity<?> createLaptop(@PathVariable("id") String storeId) {
        Optional<StoreEntity> storeEntity = storeRepository.findById(UUID.fromString(storeId));

        if (storeEntity.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        LaptopEntity laptop = new LaptopEntity(UUID.randomUUID(), "Laptop " + UUID.randomUUID(), "Descr", LocalDateTime.now(), storeEntity.get());
        return new ResponseEntity<>(laptopRepository.save(laptop), HttpStatus.OK);
    }
}
