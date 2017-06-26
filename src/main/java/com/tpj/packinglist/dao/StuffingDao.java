/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Stuffing;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author faheem
 */
public interface StuffingDao extends PagingAndSortingRepository<Stuffing, Integer> {

    @Query("from Stuffing a where a.kota.id = :idKota")
    public Page<Stuffing> filterByIdKota(@Param("idKota") Integer idKota, Pageable pageable);

    @Query("from Stuffing a where a.emkl.id = :idEmkl")
    public Page<Stuffing> filterByIdEmkl(@Param("idEmkl") Integer idEmkl, Pageable pageable);

    @Query("from Stuffing a where a.satuanKirim.id = :idSatuanKirim")
    public Page<Stuffing> filterByIdSatuanKirim(@Param("idSatuanKirim") Integer idSatuanKirim, Pageable pageable);

    @Query("from Stuffing a where a.kapalBerangkat.id = :idKapalBerangkat")
    public Page<Stuffing> filterByIdKapalBerangkat(@Param("idKapalBerangkat") Integer idKapalBerangkat, Pageable pageable);

    @Query("from Stuffing a where a.tglClosing = :tglClosing")
    public Page<Stuffing> filterByTglClosing(@Param("tglClosing") Date tglClosing, Pageable pageable);

    @Query("from Stuffing a where upper(coalesce(a.kontainer.nomor,'')) like :search or upper(coalesce(a.emkl.nama,'')) like upper(:search) or upper(coalesce(a.kapalBerangkat.kapal.nama,'')) like upper(:search) or upper(coalesce(a.satuanKirim.nama,'')) like upper(:search)")
    public Page<Stuffing> filter(@Param("search")String search, Pageable pr);

}
