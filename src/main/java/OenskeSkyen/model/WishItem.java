package OenskeSkyen.model;

// Denne klasse repræsenterer et ønske-element i systemet
public class WishItem {
    // Unikt ID for ønske-elementet
    private Long id;
    // Navnet på ønske-elementet
    private String itemName;
    // Beskrivelse af ønske-elementet
    private String description;
    // Kategori, som ønske-elementet tilhører
    private String category;
    // Prisen på ønske-elementet
    private Double price;
    // Status for om elementet er reserveret (1 = reserveret, 0 = ikke reserveret)
    private Integer isReserved;

    // Getter-metode til at hente ID'et for ønske-elementet
    public Long getId() {
        return id;
    }

    // Setter-metode til at indstille ID'et for ønske-elementet
    public void setId(Long id) {
        this.id = id;
    }

    // Getter-metode til at hente navnet på ønske-elementet
    public String getItemName() {
        return itemName;
    }

    // Setter-metode til at indstille navnet på ønske-elementet
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Getter-metode til at hente beskrivelsen af ønske-elementet
    public String getDescription() {
        return description;
    }

    // Setter-metode til at indstille beskrivelsen af ønske-elementet
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter-metode til at hente kategorien for ønske-elementet
    public String getCategory() {
        return category;
    }

    // Setter-metode til at indstille kategorien for ønske-elementet
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter-metode til at hente prisen på ønske-elementet
    public Double getPrice() {
        return price;
    }

    // Setter-metode til at indstille prisen på ønske-elementet
    public void setPrice(Double price) {
        this.price = price;
    }

    // Getter-metode til at hente reserveringsstatus for ønske-elementet
    public Integer getIsReserved() {
        return isReserved;
    }

    // Setter-metode til at indstille reserveringsstatus for ønske-elementet
    public void setIsReserved(Integer isReserved) {
        this.isReserved = isReserved;
    }
}
