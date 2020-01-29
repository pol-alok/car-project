package com.alok.projectcars.services;

import com.alok.projectcars.dao.model.Driver;
import com.alok.projectcars.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    DriverRepository driverRepository;

    public void save(Driver driver) {
        driverRepository.save(driver);
    }

    public List<Driver> listAll() {
        return driverRepository.findAll();
    }

    public Driver get(Long id) {
        return driverRepository.findById(id).get();
    }
    public Driver findByName(String name) {
        return driverRepository.findByName(name).get();
    }
}
