package com.alok.projectcars.services;

import com.alok.projectcars.dao.model.Car;
import com.alok.projectcars.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
public class CarService {

    @Autowired
    CarRepository carRepository;

    public List<Car> listAll() {
        return carRepository.findAll();
    }

    public void save(Car car) {
        carRepository.save(car);
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }

    public Car get(Long id) {
        return carRepository.findById(id).get();
    }
}
