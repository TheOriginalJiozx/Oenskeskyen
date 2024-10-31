package OenskeSkyen.model;

public class WishListItem {
    private Long id;
    private String itemName;
    private String description;
    private Long userId; // Use Long userId to reference the user instead of a User object

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
        return userId; // Getter for userId
    }

    public void setUserId(Long userId) {
        this.userId = userId; // Setter for userId
    }
}