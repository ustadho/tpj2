/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author faheem
 */
@Entity
@Table(name = "t_surat_jalan_detail")
public class SuratJalanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double p;

    @Column
    private Double l;

    @Column
    private Double t;

    @Column
    private Integer coli;

    @Column(name = "fix_volume")
    private Double fixVolume;

    @Column
    private Boolean paket;

    @Column(columnDefinition = "text")
    private String spesifikasi;
    
    @Column(columnDefinition = "text")
    private String catatan;

    @ManyToOne
    @JoinColumn(name = "id_surat_jalan")
    @JsonBackReference
    private SuratJalan suratJalan;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "sjDetail", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<SJStuffing> sjStufings = new HashSet<SJStuffing>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getL() {
        return l;
    }

    public void setL(Double l) {
        this.l = l;
    }

    public Double getT() {
        return t;
    }

    public void setT(Double t) {
        this.t = t;
    }

    public Double getFixVolume() {
        return fixVolume;
    }

    public void setFixVolume(Double fixVolume) {
        this.fixVolume = fixVolume;
    }

    public Boolean getPaket() {
        return paket;
    }

    public void setPaket(Boolean paket) {
        this.paket = paket;
    }

    public String getSpesifikasi() {
        return spesifikasi;
    }

    public void setSpesifikasi(String spesifikasi) {
        this.spesifikasi = spesifikasi;
    }

    public SuratJalan getSuratJalan() {
        return suratJalan;
    }

    public void setSuratJalan(SuratJalan suratJalan) {
        this.suratJalan = suratJalan;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getColi() {
        return coli;
    }

    public void setColi(Integer coli) {
        this.coli = coli;
    }

    public Set<SJStuffing> getSjStufings() {
        return sjStufings;
    }

    public void setSjStufings(Set<SJStuffing> sjStufings) {
        this.sjStufings = sjStufings;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

}
