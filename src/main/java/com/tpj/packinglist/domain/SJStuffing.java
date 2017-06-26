/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author faheem
 */
@Entity
@Table(name = "t_sj_stuffing")
public class SJStuffing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private Integer coli;

    @ManyToOne
    @JoinColumn(name = "id_stuffing", nullable = false)
    private Stuffing stuffing;

    @ManyToOne
    @JoinColumn(name = "id_sj_detail", nullable = false)
    @JsonBackReference
    private SuratJalanDetail sjDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Stuffing getStuffing() {
        return stuffing;
    }

    public void setStuffing(Stuffing stuffing) {
        this.stuffing = stuffing;
    }

    public SuratJalanDetail getSjDetail() {
        return sjDetail;
    }

    public void setSjDetail(SuratJalanDetail sjDetail) {
        this.sjDetail = sjDetail;
    }

    public Integer getColi() {
        return coli;
    }

    public void setColi(Integer coli) {
        this.coli = coli;
    }

}
