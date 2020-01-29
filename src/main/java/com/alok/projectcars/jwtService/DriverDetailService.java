package com.alok.projectcars.jwtService;

import com.alok.projectcars.dao.model.Driver;
import com.alok.projectcars.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DriverDetailService implements UserDetailsService {
    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Driver driver = driverRepository.findByName(s).get();
        if(driver==null) {
            throw  new UsernameNotFoundException("Author 404!");
        }
        return new DriverDetailImp(driver.getName(),driver.getPassword(),driver.getRole());
    }
}
