/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Emkl;
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
public interface EmklDao extends PagingAndSortingRepository<Emkl, Integer> {

    @Query("from Emkl a where a.kota.id = :idKota")
    public Page<Emkl> filterByIdKota(@Param("idKota") Integer idKota, Pageable pageable);
    
    @Query("from Emkl a where upper(a.nama) like upper(:search)")
    public Page<Emkl> filter(@Param("search") String search, Pageable pageable);

}
