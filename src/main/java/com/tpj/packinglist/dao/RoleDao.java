/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Menu;
import com.tpj.packinglist.domain.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cak-ust
 */
public interface RoleDao extends PagingAndSortingRepository<Role, String>{
    @Query("select r.menuSet from Role r where r.id=:roleId")
    public Set<Menu> findMenuSetRole(@Param("roleId") String roleId);
    
}
