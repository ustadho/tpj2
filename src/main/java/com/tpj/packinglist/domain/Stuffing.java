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
@Table(name = "t_stuffing")
public class Stuffing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "tgl_closing")
    private Date tglClosing;

    @ManyToOne
    @JoinColumn(name = "id_kapal_berangkat")
    private KapalBerangkat kapalBerangkat;

    @ManyToOne
    @JoinColumn(name = "id_kota")
    private Kota kota;

    @ManyToOne
    @JoinColumn(name = "id_emkl")
    private Emkl emkl;

    @ManyToOne
    @JoinColumn(name = "id_satuan_kirim")
    private SatuanKirim satuanKirim;

    @ManyToOne
    @JoinColumn(name = "id_kontainer", nullable = false)
    private Kontainer kontainer;
    
    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean aktif=Boolean.TRUE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTglClosing() {
        return tglClosing;
    }

    public void setTglClosing(Date tglClosing) {
        this.tglClosing = tglClosing;
    }

    public Kota getKota() {
        return kota;
    }

    public void setKota(Kota kota) {
        this.kota = kota;
    }

    public KapalBerangkat getKapalBerangkat() {
        return kapalBerangkat;
    }

    public void setKapalBerangkat(KapalBerangkat kapalBerangkat) {
        this.kapalBerangkat = kapalBerangkat;
    }

    public Emkl getEmkl() {
        return emkl;
    }

    public void setEmkl(Emkl emkl) {
        this.emkl = emkl;
    }

    public SatuanKirim getSatuanKirim() {
        return satuanKirim;
    }

    public void setSatuanKirim(SatuanKirim satuanKirim) {
        this.satuanKirim = satuanKirim;
    }

    public Kontainer getKontainer() {
        return kontainer;
    }

    public void setKontainer(Kontainer kontainer) {
        this.kontainer = kontainer;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

}
