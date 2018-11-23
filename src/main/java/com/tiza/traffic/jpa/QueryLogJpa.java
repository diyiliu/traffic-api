package com.tiza.traffic.jpa;

import com.tiza.traffic.jpa.dto.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Description: QueryLogJpa
 * Author: DIYILIU
 * Update: 2018-11-14 09:03
 */
public interface QueryLogJpa extends JpaRepository<QueryLog,String> {

    List<QueryLog> findByLicenseAndQueryDateAfter(String license, Date date);
}
