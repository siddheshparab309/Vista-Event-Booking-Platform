package com.events.superapp.modules.movies.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
@Table(name = "theaters")
@Getter
@Setter
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Seat> seats;

    @PrePersist
    @PreUpdate
    public void syncLocation() {
        if (latitude != null && longitude != null) {
            this.location = new org.locationtech.jts.geom.GeometryFactory()
                    .createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude));
            this.location.setSRID(4326);
        }
    }

}