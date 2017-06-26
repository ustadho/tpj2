/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Merk;
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
public interface MerkDao extends PagingAndSortingRepository<Merk, Integer> {

    @Query("from Merk a where a.toko.id = :idToko")
    public Page<Merk> filterByIdToko(@Param("idToko") Integer idToko, Pageable pageable);

}
