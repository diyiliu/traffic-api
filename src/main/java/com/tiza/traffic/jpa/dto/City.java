package com.tiza.traffic.jpa.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description: CityInfo
 * Author: DIYILIU
 * Update: 2018-11-07 14:54
 */

@Data
@Entity
@Table(name = "City")
public class City {

    private String provinceName;

    private String provinceCode;

    private String cityName;

    @Id
    private String cityCode;

    private Integer requireEngine;

    private Integer engineDigits;

    private Integer requireChassis;

    private Integer chassisDigits;
}
