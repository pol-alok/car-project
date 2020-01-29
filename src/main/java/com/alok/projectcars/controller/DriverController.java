package com.alok.projectcars.controller;

import com.alok.projectcars.dao.model.AuthenticationRequest;
import com.alok.projectcars.dao.model.AuthenticationResponse;
import com.alok.projectcars.dao.model.Car;
import com.alok.projectcars.dao.model.Driver;
import com.alok.projectcars.exception.CarAlreadyInUseException;
import com.alok.projectcars.jwtService.DriverDetailService;
import com.alok.projectcars.services.CarService;
import com.alok.projectcars.services.DriverService;
import com.alok.projectcars.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    DriverService driverService;

    @Autowired
    CarService carService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    DriverDetailService driverDetailService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> getDriverWithFilterWithOutFilter(@RequestParam(required = false,name = "manufacturer") String manufacturer,
                                                              @RequestParam(required = false, name = "engineType") String engineType) {
        List<Driver> list;
        System.out.println(manufacturer+" "+engineType);
        if(manufacturer != null) {

            list =  driverService.listAll().stream()
                    .filter((driver) ->  {
                        if(driver.getCar()!=null) {
                            return driver.getCar().getManufacturer().equals(manufacturer);
                        } else {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

        } else if(engineType!=null) {
            list =  driverService.listAll().stream()
                    .filter((driver) -> driver.getCar().getEngineType().equals(engineType))
                    .collect(Collectors.toList());
        } else  {
            list =  driverService.listAll();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @PostMapping("/signUp")
    public ResponseEntity<?> driverSignUp(@RequestParam(name = "name") String name,
                                          @RequestParam(required = false, name = "password") String pass) {

        String encryptedPass = passwordEncoder.encode(pass);
        Driver driver = new Driver(null,name,encryptedPass,"DRIVER",null);
        try {
            driverService.save(driver);
            return new ResponseEntity<>(driver, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error in saving the data",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        System.out.println("controller is here");
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    ));

        } catch (BadCredentialsException e) {

            return new ResponseEntity<>("Incorrect username or password", HttpStatus.FORBIDDEN);
        }


        final UserDetails userDetails = driverDetailService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/select")
    public ResponseEntity<?> selectCarByDriver(@RequestParam(required = false, name = "carId") Long carId, Principal principal) throws CarAlreadyInUseException {

        Car car = carService.get(carId);
        Driver driver = driverService.findByName(principal.getName());
            if(car.getStatus()==null || car.getStatus()==false) {
                car.setStatus(true);
                driver.setCar(car);
                driverService.save(driver);
                return new ResponseEntity<>(driver, HttpStatus.OK);
            } else  {
                throw new CarAlreadyInUseException("Car is is already used by other driver with Id : " + driver.getId());
            }

    }
    @PostMapping("/deselect")
    public ResponseEntity<?> deselectCarByDriver(@RequestParam(name = "driverId") Long driverId,
                                              @RequestParam(required = false, name = "carId") Long carId){

        Driver driver = driverService.get(driverId);
        driver.setCar(null);
        try {
            driverService.save(driver);
            return new ResponseEntity<>(driver, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error in saving the data",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
