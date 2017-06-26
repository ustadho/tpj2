/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.SJStuffing;
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
public interface SJStuffingDao extends PagingAndSortingRepository<SJStuffing, Integer> {

    @Query("from SJStuffing a where a.stuffing.id = :idStuffing")
    public Page<SJStuffing> filterByIdStuffing(@Param("idStuffing") Integer idStuffing, Pageable pageable);

    @Query("from SJStuffing a "
            + "left join fetch a.sjDetail "
            + "where a.sjDetail.id = :idSjDetail")
    public List<SJStuffing> filterByIdSjDetail(@Param("idSjDetail") Integer idSjDetail);

}
