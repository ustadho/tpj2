/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author faheem
 */
@Entity
@Table(name = "t_bahan_sj")
public class BahanSj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "text")
    private String keterangan;

    @Column(name = "user_ins")
    private String userIns;

    @Column(name = "user_upd")
    private String userUpd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tgl_ins")
    private Date tglIns;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tgl_upd")
    private Date tglUpd;

    @OneToMany(mappedBy = "bahan", cascade = {javax.persistence.CascadeType.ALL}, orphanRemoval = true)
    private List<BahanSjDetail> listBahanDetail = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public List<BahanSjDetail> getListBahanDetail() {
        return listBahanDetail;
    }

    public void setListBahanDetail(List<BahanSjDetail> listBahanDetail) {
        this.listBahanDetail = listBahanDetail;
    }

    public Date getTglIns() {
        return tglIns;
    }

    public void setTglIns(Date tglIns) {
        this.tglIns = tglIns;
    }

    public Date getTglUpd() {
        return tglUpd;
    }

    public void setTglUpd(Date tglUpd) {
        this.tglUpd = tglUpd;
    }

    public String getUserIns() {
        return userIns;
    }

    public void setUserIns(String userIns) {
        this.userIns = userIns;
    }

    public String getUserUpd() {
        return userUpd;
    }

    public void setUserUpd(String userUpd) {
        this.userUpd = userUpd;
    }

}
