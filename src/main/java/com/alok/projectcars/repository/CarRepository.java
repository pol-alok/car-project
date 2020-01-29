package com.alok.projectcars.repository;

import com.alok.projectcars.dao.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Long> {

    Optional<Car> findById(Long id);
}
