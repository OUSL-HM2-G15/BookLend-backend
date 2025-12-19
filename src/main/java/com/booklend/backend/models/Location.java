package com.booklend.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Location entity class that represents the "locations" table in the database.
 * The locations represent physical places that users can be associated with or books can be located in.
 */
@Entity
@Table(name = "locations") // This maps the Location class to the 'locations' table in DB.
@Getter // Lombok generates getter methods
@Setter // Lombok generates setter methods

public class Location {
   /**
     * Primary Key (PK) for the Location table.
     * Auto-incremented by MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id") // Maps to the 'location_id' column in DB.
    private Long locationId;

    /**
     * The name of the location (e.g., "Colombo", "Kandy").
     * This column is marked as 'NOT NULL' in the database schema.
     */
    @Column(name = "location_name", nullable = false)
    private String locationName;

    // Default constructor required by JPA (Hibernate)
    public Location() {}


    @Override
    public String toString() {
        return "Location{id=" + locationId + ", name='" + locationName + "'}";
    }
}
