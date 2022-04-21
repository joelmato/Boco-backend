package ntnu.idatt.boco.controller;

import ntnu.idatt.boco.model.Rental;
import ntnu.idatt.boco.repository.RentalRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class RentalController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    RentalRepository rentalRepository;

    @PostMapping("/rental")
    public ResponseEntity<String> registerNewRental(@RequestBody Rental rental) {
        logger.info("New rental registration requested");
        try {
            rentalRepository.saveRentalToDatabase(rental);
            logger.info("Success - rental registered");
            return new ResponseEntity<>("Registered successfully!", HttpStatus.CREATED);
        } catch(Exception e) {
            logger.error("Rental registration error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}