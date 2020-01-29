package com.alok.projectcars.controller;

import com.alok.projectcars.dao.model.Car;
import com.alok.projectcars.domain.CarDomain;
import com.alok.projectcars.services.CarService;
import com.alok.projectcars.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    CarService carService;

    @Autowired
    DriverService driverService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCars() {

        List< ? extends Car> resultList;

        resultList = carService.listAll();

        System.out.println(resultList.size());
        if(resultList != null && resultList.size() > 0) {
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } else  {
           return new ResponseEntity<>("No content Found",HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<?> newCar(@RequestParam(required = false, name = "name") String name,
                                    @RequestParam(required = false, name = "model") String model,
                                    @RequestParam(name = "licensePlate") String licensePlate,
                                    @RequestParam(name = "seatCount") Integer seatCount,
                                    @RequestParam(name = "convertible") Boolean convertible,
                                    @RequestParam(name = "rating") Double rating,
                                    @RequestParam(name = "engineType") String engineType,
                                    @RequestParam(name = "manufacturer") String manufacturer, Principal principal) {


            String roleOfCurrentUser = driverService.findByName(principal.getName()).getRole();
            ResponseEntity<?> response;
            if(roleOfCurrentUser.equals("ADMIN")) {
                Car car = new Car(null,name,model,licensePlate,seatCount,convertible,rating,engineType,manufacturer,null,null,null);

                try {
                    carService.save(car);
                    response = new ResponseEntity<>(new CarDomain(car.getId(),car.getName()),HttpStatus.CREATED);
                }
                catch (Exception e) {
                    response = new ResponseEntity<>("Error in saving the data",HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else  {
                response = new ResponseEntity<>("It seems you have't access to this level ",HttpStatus.FORBIDDEN);
            }

            return response;
    }

    @PutMapping
    public ResponseEntity<?> upgradeCar(@RequestParam(name = "id") Long id,
                                        @RequestParam(required = false, name = "name") String name,
                                        @RequestParam(required = false, name = "model") String model,
                                        @RequestParam(name = "licensePlate") String licensePlate,
                                        @RequestParam(name = "seatCount") Integer seatCount,
                                        @RequestParam(name = "convertible") Boolean convertible,
                                        @RequestParam(name = "rating") Double rating,
                                        @RequestParam(name = "engineType") String engineType,
                                        @RequestParam(name = "manufacturer") String manufacturer) {
        Car car = carService.get(id);

        car.setName(name);
        car.setModel(model);
        car.setLicensePlate(licensePlate);
        car.setSeatCount(seatCount);
        car.setConvertible(convertible);
        car.setRating(rating);
        car.setEngineType(engineType);
        car.setManufacturer(manufacturer);
        try {
            carService.save(car);
            return new ResponseEntity<>(new CarDomain(car.getId(),car.getName()),HttpStatus.OK);
        }
        catch (Exception e) {
           return new ResponseEntity<>("Error in saving the data",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCar(@RequestParam(name = "id") Long id) {
        ResponseEntity<?> response;
        try {
            carService.delete(id);
            response = new ResponseEntity<>("Successfully deleted car with ID : "+id,HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response = new ResponseEntity<>("Error in deleting the data",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
