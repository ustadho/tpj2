/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao.jdbc;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cak-ust
 */
@Repository
public class MenuDaoJdbc {
    @Autowired
    MapResultSet mr;
    
    @Autowired
    DataSource dataSource;
    
    public Object daftarMenu(String id){
        String query="SELECT m.id, m.menu_action, m.label, m.menu_level, m.menu_order\n" +
                    "FROM \n" +
                    "  c_security_menu m \n" +
                    "  inner join c_security_role_menu rm on rm.id_menu = m.id\n" +
                    "  inner join c_security_role r on r.id = rm.id_role\n" +
                    "  inner join c_security_user u on u.id_role = r.id\n" +
                    "WHERE \n" +
                    "  u.id='"+id+"'\n" +
                    "order by m.menu_level , m.menu_order ";
        return mr.mapList(query);
    }
    
}
