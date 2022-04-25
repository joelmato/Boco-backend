package ntnu.idatt.boco.model;

import java.time.LocalDate;

public class Rental {
    private int rentalId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private int productId;
    private int userId;

    public Rental() {}
    
    public Rental(int rentalId, LocalDate dateFrom, LocalDate dateTo, int productId, int userId) {
        this.rentalId = rentalId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.productId = productId;
        this.userId = userId;
    }

    public int getRentalId() { 
        return rentalId; 
    }
    public LocalDate getDateFrom() { 
        return dateFrom; 
    }
    public LocalDate getDateTo() { 
        return dateTo; 
    }
    public int getProductId() { 
        return productId; 
    }
    public int getUserId() { 
        return userId; 
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }
    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", productId=" + productId +
                ", userId=" + userId +
                '}';
    }
}
