package com.alok.projectcars.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String model;
    private String licensePlate;
    private Integer seatCount;
    private Boolean convertible;
    private Double rating;
    private String engineType;
    private String manufacturer;
    private Boolean status;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date createdDate;

    @JsonIgnore
    @OneToOne(mappedBy = "car",cascade = {CascadeType.ALL})
    private Driver driver;
}
