package com.tiza.traffic.jpa.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description: CityLicense
 * Author: DIYILIU
 * Update: 2018-11-09 09:38
 */

@Data
@Entity
@Table(name = "CityLicense")
public class CityLicense {

    @Id
    private Long id;

    private String cityName;

    private String cityCode;

    private String licensePrefix;
}
