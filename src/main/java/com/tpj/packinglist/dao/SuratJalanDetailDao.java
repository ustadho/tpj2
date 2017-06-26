/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.SuratJalanDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author faheem
 */
public interface SuratJalanDetailDao extends PagingAndSortingRepository<SuratJalanDetail, Integer> {

//    @Query("from SuratJalanDetail a where a.suratJalan.id = :idSuratJalan")
//    public Page<SuratJalanDetail> filterByIdSuratJalan(@Param("idSuratJalan") Integer idSuratJalan, Pageable pageable);

}
