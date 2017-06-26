/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.BahanSj;
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
public interface BahanSjDao extends PagingAndSortingRepository<BahanSj, Integer> {

    @Query("from BahanSj a where upper(a.keterangan) like upper(:search)")
    public Page<BahanSj> filter(@Param("search") String search, Pageable pageable);

}
