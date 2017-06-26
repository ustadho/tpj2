/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.KapalBerangkat;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author faheem
 */
public interface KapalBerangkatDao extends PagingAndSortingRepository<KapalBerangkat, Integer> {

    @Query("from KapalBerangkat a where a.tglBerangkat = :tglBerangkat")
    public Page<KapalBerangkat> filterByTanggalBerangkat(@Param("tglBerangkat") Date tglBerangkat, Pageable pageable);

    @Query("from KapalBerangkat a where a.kota.id = :idKota and a.aktif = true order by a.tglBerangkat desc")
    public List<KapalBerangkat> filterByIdKotaAndAktif(@Param("idKota") Integer idKota);

    @Query("from KapalBerangkat a where upper(a.kapal.nama) like upper(:search) or upper(a.kota.nama) like upper(:search)")
    public Page<KapalBerangkat> filter(@Param("search") String search, Pageable pageable);
    
}
