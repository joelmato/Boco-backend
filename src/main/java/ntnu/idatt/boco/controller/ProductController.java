package ntnu.idatt.boco.controller;

import lombok.NoArgsConstructor;
import ntnu.idatt.boco.model.*;
import ntnu.idatt.boco.repository.ImageRepository;
import ntnu.idatt.boco.repository.ProductRepository;
import ntnu.idatt.boco.repository.RentalRepository;
import ntnu.idatt.boco.repository.UserRepository;
import ntnu.idatt.boco.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods responsible for handling HTTP requests regarding {@link Product}
 */
@CrossOrigin
@RestController
@RequestMapping("api/products")
@NoArgsConstructor
public class ProductController {
    Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired ProductRepository productRepository;
    @Autowired RentalRepository rentalRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired ProductService service;
    @Autowired UserRepository userService;

    /**
     * Method for handling POST-requests for registering a new product
     * @param product the product to be registered
     * @return an HTTP response containing a result message as a String and a HTTP status code
     */
    @PostMapping
    public ResponseEntity<String> newProduct(@RequestBody Product product) {
        logger.info("Creating new product: " + product.getTitle());
        try {
            productRepository.newProduct(product);
            logger.info("Product created");
            int id = productRepository.getProductByTitle(product.getTitle()).getProductId();
            for (ProductImage image : product.getImages()) {
                logger.info("Image: " + image.toString());
                image.setProductId(id);
                imageRepository.newPicture(image);
            }
            return new ResponseEntity<>("Created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating new product");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling PUT-requests for editing a product
     * @param productId the id of the product
     * @param product   the edited product
     * @return an HTTP response containing a result message as a String and an HTTP status code
     */
    @PutMapping("/{productId}")
    public ResponseEntity<String> editProduct(@PathVariable int productId, @RequestBody Product product) {
        logger.info("Product " + productId + " - editing product");
        try {
            productRepository.editProduct(product, productId);
            imageRepository.deleteProductsImages(productId);
            if (product.getImages() != null) {
                for (ProductImage image : product.getImages()) {
                    logger.info("Image: " + image.toString());
                    image.setProductId(productId);
                    imageRepository.newPicture(image);
                }
            }
            return new ResponseEntity<>("Created successfully!", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error editing product");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling GET-requests for retrieving a product by id
     * @param productId the id of the product
     * @return an HTTP response containing the retrieved product and an HTTP status code
     */
    @GetMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<Product> getById(@PathVariable int productId) {
        logger.info("Product " + productId + " - getting product");
        try {
            return new ResponseEntity<>(productRepository.getProduct(productId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting product");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling GET-requests for retrieving a products availability window
     * @param productId the id of the product
     * @return an HTTP response containing a list of the availability windows of the product and an HTTP status code
     */
    @GetMapping("/{productId}/availability")
    @ResponseBody
    public ResponseEntity<List<AvailabilityWindow>> getAvailability(@PathVariable int productId) {
        logger.info("Product " + productId + " - getting availability window");
        try {
            Product product = productRepository.getProduct(productId);
            List<Rental> rentals = rentalRepository.getAcceptedRentals(productId, true);
            return new ResponseEntity<>(service.getAvailability(product, rentals), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Could not get availability");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling POST-request for adding new images
     * @param productId the id of the product to add the image to
     * @param images     the images to be added
     * @return an HTTP response containing a result message as a String and an HTTP status code
     */
    @PostMapping("/{productId}/image")
    public ResponseEntity<String> newImage(@PathVariable int productId, @RequestBody ArrayList<ProductImage> images) {
        logger.info("Product " + productId + " - adding " + images.size() + " images");
        try {
            for (ProductImage image : images) {
                imageRepository.newPicture(image);
            }
            return new ResponseEntity<>("Created successfully!", HttpStatus.CREATED);
        }catch (Exception e) {
            logger.error("Error saving new product");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling GET-requests for retrieving a products images
     * @param productId the id of the product
     * @return an HTTP response containing a list of product images and an HTTP status code
     */
    @GetMapping("/{productId}/image")
    @ResponseBody
    public ResponseEntity<List<ProductImage>> getImagesByProductId(@PathVariable int productId) {
        logger.info("Product " + productId + " getting images");
        try {
            List<ProductImage> images = imageRepository.getImagesByProductId(productId);
            logger.info("Product " + productId + " - " + images.size() + " images found");
            return new ResponseEntity<>(images, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting images");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling GET-requests for searching for products
     * @param q the word to search for
     * @param category the category to search for
     * @param sortBy the option to sort by
     * @param ascending the order true = asc, false = desc
     * @return  a list of all the products matching the search-word
     */
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Product>> getProductFromSearch(@RequestParam(required = false) String q, @RequestParam(required = false) String category, @RequestParam String sortBy, @RequestParam boolean ascending) {
        try {
            if (category == null || category.isBlank()) {
                if (q == null || q.isBlank()) {
                    logger.info("Searching sorted by" + sortBy + " " + ascending);
                    return new ResponseEntity<>(productRepository.getAll(sortBy, ascending), HttpStatus.OK);
                } else {
                    logger.info("Searching for " + q + " sorted by" + sortBy + " " + ascending);
                    return new ResponseEntity<>(productRepository.searchProductByWord(q, sortBy, ascending), HttpStatus.OK);
                }
            } else {
                if (q == null || q.isBlank()) {
                    logger.info("Searching for " + category + " sorted by" + sortBy + " " + ascending);
                    return new ResponseEntity<>(productRepository.searchProductByCategory(category, sortBy, ascending), HttpStatus.OK);
                } else {
                    logger.info("Searching for " + q + " and " + category + " sorted by" + sortBy + " " + ascending);
                    return new ResponseEntity<>(productRepository.searchProductByWordAndCategory(q, category, sortBy, ascending), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            logger.error("Could not search for a product");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling POST-requests for retrieving all of a users products
     * @param userId the id of the user to retrieve all the products for
     * @return an HTTP response containing a list of all the users products and an HTTP status code
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<UsersProducts> getUsersProducts(@PathVariable int userId) {
        logger.info("Getting users " + userId + "products");
        try {
            User user = userService.getUserById(userId);
            List<Product> products = productRepository.getFromUserId(userId);
            return new ResponseEntity<>(new UsersProducts(user, products), HttpStatus.OK);
        }catch (Exception e ){
            logger.error("Getting users products failed");
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for handling POST-requests for retrieving a users rental history
     * @param userId the id of the user to retrieve the history for
     * @return an HTTP response containing a list of the users history and an HTTP status code
     */
    @GetMapping("/user/{userId}/history")
    public ResponseEntity<List<Product>> getUserRentalHistory(@PathVariable int userId) {
        logger.info("User " + userId + " - getting rental history");
        try {
            List<Product> history = productRepository.getUserRentalHistory(userId);
            if (history.isEmpty()) {
                logger.info("User " + userId + " - rental history is empty.");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                logger.info("Rental history for user " + userId + " retrieved successfully.");
                return new ResponseEntity<>(history, HttpStatus.OK);
            }
        }catch (Exception e ){
            logger.error("Rental history retrieval failed");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  Method for handling DELETE-request for deleting a product based on product id and user id
     * @param userId the id of the owner of the product
     * @param productId the id of the product that is to be deleted
     * @return an HTTP response containing a response string and an HTTP status code
     */
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<String> deleteProductWithuserId(@PathVariable int userId, @RequestParam int productId){
        try{
            logger.info("Attempting to delete product");
            if(productRepository.deleteProductWithUserIdAndProductId(userId,productId) == 1){
                logger.info("Deletion for product with product id " + productId + " was successful");
                return new ResponseEntity<>("Deletion for product with product id " + productId + " was successful", HttpStatus.OK);
            }else {
                logger.info("Wrong product id or user id ");
                return new ResponseEntity<>("Wrong product id or user id", HttpStatus.CONFLICT);
            }

        }catch (Exception e){
            logger.error("Deletion was unsuccessful for product with product id " + productId + " for user with user id" + userId);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
