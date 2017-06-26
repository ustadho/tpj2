/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Menu;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cak-ust
 */
public interface MenuDao extends PagingAndSortingRepository<Menu, String>{
    @Query("select m from Menu m " +
			"order by m.level, m.order")
    public Page<Menu> findAllOrderLevelOrder(Pageable pageable);
    
    @Query("select m from Menu m " +
			"where m.parent is null " +
			"order by m.level, m.order")
    public List<Menu> findTopLevelMenu();

    @Query("select m from Menu m " +
			"where m.parent.id = :id " +
			"order by m.level, m.order")
    public List<Menu> findMenuByParent(@Param("id") String id);

    public List<Menu> findByIdNotIn(List<String> ids);
    
    @Query("select m from Menu m where upper(m.title) like upper(:search) or upper(m.stateRef) like upper(:search) or upper(m.name) like upper(:search) "
            + "order by m.level, m.order")
    public Page<Menu> filter(@Param("search") String search ,Pageable pageable);

    
}
