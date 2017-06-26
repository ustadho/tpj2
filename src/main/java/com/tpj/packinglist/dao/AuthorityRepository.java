/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao;

import com.tpj.packinglist.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author ustadho
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

