package com.tiza.traffic.jpa;

import com.tiza.traffic.jpa.dto.CityLicense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description: CityLicenseJpa
 * Author: DIYILIU
 * Update: 2018-11-09 09:40
 */

public interface CityLicenseJpa extends JpaRepository<CityLicense, Long> {

    CityLicense findByLicensePrefix(String prefix);
}
