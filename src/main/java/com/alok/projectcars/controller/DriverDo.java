package com.alok.projectcars.controller;

import com.alok.projectcars.dao.model.Car;
import com.alok.projectcars.dao.model.Driver;
import com.alok.projectcars.services.DriverService;

public class DriverDo {
    public Boolean selectCar(Car car, Driver driver, DriverService driverService) {
        if(car.getStatus()==null || car.getStatus()==false) {
            car.setStatus(true);
            driver.setCar(car);
            driverService.save(driver);
            return true;
        } else  {
            return false;
        }
    }

}
