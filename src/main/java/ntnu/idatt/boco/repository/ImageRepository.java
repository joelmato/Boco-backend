package ntnu.idatt.boco.repository;

import ntnu.idatt.boco.model.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class is responsible for communication with the database regarding {@link ProductImage}.
 */
@Repository
public class ImageRepository {
    @Autowired JdbcTemplate jdbcTemplate;

    /**
     * Method for adding a new product image to the database
     * @param image the product image to be added
     */
    public void newPicture(ProductImage image) {
        jdbcTemplate.update("INSERT INTO images(img_name, img_blob, product_id, img_data) VALUES (?,?,?,?);",
                new Object[] {image.getImgName(), image.getImgBlob(), image.getProductId(), image.getImgData()});
    }

    /**
     * Method for retrieving all product images of a product
     * @param productId the id of the product
     * @return a list of all the product images of a product
     */
    public List<ProductImage> getImagesByProductId(int productId) {
        return jdbcTemplate.query("SELECT * FROM images WHERE product_id = ?",
                BeanPropertyRowMapper.newInstance(ProductImage.class), productId);
    }

    /**
     * Method for retrieving the images of a users products
     * @param userId the id of the user
     * @return a list of product images of the users products
     */
    public List<ProductImage> getImagesForUsersProducts(int userId) {
        return jdbcTemplate.query("SELECT * FROM images INNER JOIN products WHERE products.user_id = ? AND img_id IN (SELECT min(img_id) FROM images GROUP BY images.product_id);",
                BeanPropertyRowMapper.newInstance(ProductImage.class), userId);
    }

    /**
     * Method for deleting a products images
     * @param productId the id of the product to delete the images for
     */
    public void deleteProductsImages(int productId) {
        jdbcTemplate.update("DELETE FROM images WHERE product_id = ?", productId);
    }
}
