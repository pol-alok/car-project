package com.alok.projectcars.repository;
import com.alok.projectcars.dao.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DriverRepository extends JpaRepository<Driver,Long> {
    Optional<Driver> findById(Long id);
    Optional<Driver> findByName(String name);
}
