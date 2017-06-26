/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Kontainer;
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
public interface KontainerDao extends PagingAndSortingRepository<Kontainer, Integer> {

    public Kontainer findByNomor(String value);

    @Query("from Kontainer a where upper(nomor) like upper(:search)")
    public Page<Kontainer> filter(@Param("search") String search, Pageable pageable);

}
