package com.tiza.traffic.jpa;

import com.tiza.traffic.jpa.dto.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Description: ViolationJpa
 * Author: DIYILIU
 * Update: 2018-11-09 09:54
 */
public interface ViolationJpa extends JpaRepository<Violation, String> {

    List<Violation> findByLicenseAndOccurTimeBetweenOrderByOccurTime(String license, Date begin, Date end);

    List<Violation> findByLicenseAndHandled(String license, int handled);
}
