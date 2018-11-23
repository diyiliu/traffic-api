package com.tiza.traffic.jpa;

import com.tiza.traffic.jpa.dto.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Description: CityJpa
 * Author: DIYILIU
 * Update: 2018-11-07 14:54
 */
public interface CityJpa extends JpaRepository<City, String> {

    @Query("select c from City c inner join CityLicense l on l.cityCode = c.cityCode and l.licensePrefix = ?1")
    City findByLicensePrefix(String prefix);
}
