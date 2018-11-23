package com.tiza.traffic.jpa.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Description: QueryLog
 * Author: DIYILIU
 * Update: 2018-11-14 09:00
 */

@Data
@Entity
@IdClass(QueryLogKey.class)
@Table(name = "QueryLog")
public class QueryLog {

    @Id
    private String license;

    @Id
    private Date queryDate;
}

@Data
class QueryLogKey implements Serializable {

    private String license;

    private Date queryDate;
}
