package OenskeSkyen.model;

public class WishListItem {
    private Long id;
    private String itemName;
    private String description;
    private Long userId;
    private Double price;
    private Integer isReserved;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getIsReserved() {
        return isReserved; // Getter for isReserved
    }

    public void setIsReserved(Integer isReserved) {
        this.isReserved = isReserved; // Setter for isReserved
    }
}