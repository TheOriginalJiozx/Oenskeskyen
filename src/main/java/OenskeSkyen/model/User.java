package OenskeSkyen.model;

// Denne klasse repræsenterer en bruger i systemet
public class User {
    // Unikt ID for brugeren
    private Long id;
    // Brugernavn til brugeren
    private String username;
    // Brugerens adgangskode
    private String password;
    // Angiver, om brugeren er aktiveret eller deaktiveret
    private boolean enabled;
    // Donation forbundet med brugeren
    private Integer donation;

    // Getter-metode til at hente brugerens ID
    public Long getId() {
        return id;
    }

    // Setter-metode til at indstille brugerens ID
    public void setId(Long id) {
        this.id = id;
    }

    // Getter-metode til at hente brugernavnet
    public String getUsername() {
        return username;
    }

    // Setter-metode til at indstille brugernavnet
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter-metode til at hente adgangskoden
    public String getPassword() {
        return password;
    }

    // Setter-metode til at indstille adgangskoden
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter-metode til at kontrollere, om brugeren er aktiveret
    public boolean isEnabled() {
        return enabled;
    }

    // Setter-metode til at aktivere eller deaktivere brugeren
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Setter-metode til at indstille donationen
    public void setDonation(Integer donation) {
        this.donation = donation;
    }

    // Getter-metode til at hente donationen
    public Integer getDonation() {
        return donation;
    }
}
