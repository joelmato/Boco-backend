package ntnu.idatt.boco.controller;

import ntnu.idatt.boco.model.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Runs SpringBoot again before testing this class. Resets database.
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest
public class ProductControllerTest {
    @Autowired ProductController productController;
    private final Product EXISTING_TEST_PRODUCT = new Product(1, "Dragon hunter crossbow", "A dragonbane weapon requiring 65 Ranged to wield.", "Gilenor", 600.0, false,  LocalDate.of(2022, 4, 11), LocalDate.of(2022, 6, 20),1, "hvitevarer");
    private final Product TEST_PRODUCT = new Product(2, "Abyssal whip", "A one-handed melee weapon which requires an Attack level of 70 to wield.", "Gilenor", 300.0, false, LocalDate.of(2022, 2, 1), LocalDate.of(2022, 9, 25), 1, "utstyr");
   
    @Test
    @Order(1)
    public void successfullyRegisteredNewProduct() {
        assertEquals(201, productController.newProduct(TEST_PRODUCT).getStatusCodeValue());
    }

    @Test
    @Order(2)
    public void successfullyRetrievedAllProducts() {        
        List<Product> list = Arrays.asList(EXISTING_TEST_PRODUCT, TEST_PRODUCT);

        assertEquals(list.toString(), productController.getAll().getBody().toString());
    }

    @Test
    @Order(3)
    public void successfullyEditedProduct() {
        Product editedTestProduct = new Product("The dragon hunter crossbow possesses a passive effect that increases ranged accuracy by 30% and damage by 25% when fighting draconic creatures.", "Gilenor", 350.0, false, LocalDate.of(2022, 2, 1), LocalDate.of(2022, 9, 25), "utstyr");
        assertEquals(200, productController.editProduct(2, editedTestProduct).getStatusCodeValue());
    }

    @Test
    @Order(4)
    public void successfullyRetrievedProductById() {
        assertEquals(EXISTING_TEST_PRODUCT.toString(), productController.getById(1).getBody().toString());
    }

    @Test
    @Order(5)
    public void successfullyRetrievedProductsByCategory() {
        List<Product> list = Arrays.asList(EXISTING_TEST_PRODUCT);
        assertEquals(list.toString(), productController.getByCategory("hvitevarer").getBody().toString());
    }
}
