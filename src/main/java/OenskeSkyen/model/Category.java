package OenskeSkyen.model;

// Klasse, der repræsenterer en kategori
public class Category {
    // Unikt ID for kategorien
    private Long id;

    // Navn på kategorien
    private String name;

    // Konstruktor til initialisering af kategoriens ID og navn
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter for kategoriens ID
    public Long getId() {
        return id;
    }

    // Setter for kategoriens ID
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for kategoriens navn
    public String getName() {
        return name;
    }

    // Setter for kategoriens navn
    public void setName(String name) {
        this.name = name;
    }
}
