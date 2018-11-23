package com.tiza.traffic.jpa.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: Violation
 * Author: DIYILIU
 * Update: 2018-11-09 09:48
 */

@Entity
@IdClass(ViolationKey.class)
@Table(name = "Violation")
public class Violation {

    private String code;

    private String archiveno;

    @Id
    private String license;

    @Id
    private Date occurTime;

    private String location;

    @Id
    private String act;

    private String city;

    private Integer score;

    private Integer money;

    /**
     * 0: 未处理; 1: 已处理;
     **/
    private Integer handled;

    @Override
    public boolean equals(Object o) {

        if (o instanceof Violation) {
            Violation v = (Violation) o;
            if (v.getLicense().equals(license)) {
                if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(v.getCode())) {
                    if (v.getCode().equals(code)) {

                        return true;
                    }
                }

                if (!StringUtils.isEmpty(archiveno) && !StringUtils.isEmpty(v.getArchiveno())) {
                    if (v.getArchiveno().equals(archiveno)) {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArchiveno() {
        return archiveno;
    }

    public void setArchiveno(String archiveno) {
        this.archiveno = archiveno;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getHandled() {
        return handled;
    }

    public void setHandled(Integer handled) {
        this.handled = handled;
    }
}

@Data
class ViolationKey implements Serializable {

    private String license;

    private Date occurTime;

    private String act;
}

