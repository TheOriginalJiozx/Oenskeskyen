package OenskeSkyen.model;

// Denne klasse repræsenterer et element på en ønskeliste i systemet
public class WishListItem {
    // Unikt ID for ønskeliste-elementet
    private Long id;
    // Navnet på ønskeliste-elementet
    private String itemName;
    // Beskrivelse af ønskeliste-elementet
    private String description;
    // ID for den bruger, som ejer ønskeliste-elementet
    private Long userId;
    // Prisen på ønskeliste-elementet
    private Double price;
    // Status for om elementet er reserveret (1 = reserveret, 0 = ikke reserveret)
    private Integer isReserved;

    // Getter-metode til at hente ID'et for ønskeliste-elementet
    public Long getId() {
        return id;
    }

    // Setter-metode til at indstille ID'et for ønskeliste-elementet
    public void setId(Long id) {
        this.id = id;
    }

    // Getter-metode til at hente navnet på ønskeliste-elementet
    public String getItemName() {
        return itemName;
    }

    // Setter-metode til at indstille navnet på ønskeliste-elementet
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Getter-metode til at hente beskrivelsen af ønskeliste-elementet
    public String getDescription() {
        return description;
    }

    // Setter-metode til at indstille beskrivelsen af ønskeliste-elementet
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter-metode til at hente brugerens ID, som ejer ønskeliste-elementet
    public Long getUserId() {
        return userId;
    }

    // Setter-metode til at indstille brugerens ID, som ejer ønskeliste-elementet
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter-metode til at hente prisen på ønskeliste-elementet
    public Double getPrice() {
        return price;
    }

    // Setter-metode til at indstille prisen på ønskeliste-elementet
    public void setPrice(Double price) {
        this.price = price;
    }

    // Getter-metode til at hente reserveringsstatus for ønskeliste-elementet
    public Integer getIsReserved() {
        return isReserved; // Getter for isReserved
    }

    // Setter-metode til at indstille reserveringsstatus for ønskeliste-elementet
    public void setIsReserved(Integer isReserved) {
        this.isReserved = isReserved; // Setter for isReserved
    }
}
