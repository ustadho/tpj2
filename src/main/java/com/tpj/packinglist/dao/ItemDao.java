/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Item;
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
public interface ItemDao extends PagingAndSortingRepository<Item, Integer> {

    @Query("from Item a where a.jenisItem.id = :idJenisItem")
    public Page<Item> filterByIdJenisItem(@Param("idJenisItem") Integer idJenisItem, Pageable pageable);
    
    @Query("from Item a where upper(a.nama) like upper(:search)")
    public Page<Item> filter(@Param("search") String search, Pageable pageable);

    public Item findByNama(String value);

}
