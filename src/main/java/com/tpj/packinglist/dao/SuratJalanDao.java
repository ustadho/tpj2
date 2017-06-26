/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.SuratJalan;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author faheem
 */
public interface SuratJalanDao extends PagingAndSortingRepository<SuratJalan, Integer> {

    @Query("from SuratJalan a where a.tanggal = :tanggal")
    public Page<SuratJalan> filterByTanggal(@Param("tanggal") Date tanggal, Pageable pageable);

    @Query("from SuratJalan a where a.stuffing.id = :idStuffing")
    public Page<SuratJalan> filterByIdStuffing(@Param("idStuffing") Integer idStuffing, Pageable pageable);

    @Query("from SuratJalan a where a.toko.id = :idSuratJalan")
    public Page<SuratJalan> filterByIdSuratJalan(@Param("idSuratJalan") Integer idSuratJalan, Pageable pageable);

    @Query("from SuratJalan a where a.merk.id = :idMerk")
    public Page<SuratJalan> filterByIdMerk(@Param("idMerk") Integer idMerk, Pageable pageable);

    @Query("from SuratJalan a where a.kondisi.id = :idKondisi")
    public Page<SuratJalan> filterByIdKondisi(@Param("idKondisi") Integer idKondisi, Pageable pageable);

    @Query("from SuratJalan a where upper(a.nomorSJ) like upper(:search) or upper(a.toko.nama) like upper(:search) or upper(a.merk.nama) like upper(:search) or upper(a.kondisi.nama) like upper(:search)")
    public Page<SuratJalan> filter(@Param("search") String search, Pageable pageable);

}
