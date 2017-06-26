/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tpj.packinglist.dao.jdbc;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author faheem
 */
@Repository
public class SystemDaoJdbc {
    @Autowired
    MapResultSet mr;
    
    public Object listMenuByGroup(String idGroup){
        return mr.mapList("select menu_action, label, gambar from c_security_menu  where id_group  ='"+idGroup+"'\n" +
                          "order by menu_order");
        
    }
    public List<Map<String, Object>> listMenuGroupByUsername(String username){
        String query="select distinct m.id_group, mg.name, mg.urut\n" +
                    "from c_security_user u \n" +
                    "left join c_security_role r on r.id=u.id_role\n" +
                    "left join c_security_role_menu rm on rm.id_role=r.id\n" +
                    "left join c_security_menu m on m.id=rm.id_menu\n" +
                    "left join c_security_menu_group mg on mg.id=m.id_group\n" +
                    "where u.username='"+username+"'\n" +
                    "order by mg.urut";
        
        return mr.mapList(query);
        
    }
    public Object listMenuGroupByUsername(String username, String idGroup){
        String query="select distinct m.id, m.label, m.menu_action, m.menu_order\n" +
                    "from c_security_user u \n" +
                    "left join c_security_role r on r.id=u.id_role\n" +
                    "left join c_security_role_menu rm on rm.id_role=r.id\n" +
                    "left join c_security_menu m on m.id=rm.id_menu\n" +
                    "left join c_security_menu_group mg on mg.id=m.id_group\n" +
                    "where u.username='"+username+"'\n" +
                    "and m.id_group='"+idGroup+"'\n" +
                    "order by m.menu_order";
        return mr.mapList(query);
        
    }
    
    public List<Map<String, Object>> cekMenuAuth(String userid, String role){
        String query="select rp.* from c_security_role_permission rp\n" +
                    "inner join c_security_permission p on p.id=rp.id_permission\n" +
                    "inner join c_security_role r on r.id=rp.id_role\n" +
                    "inner join c_security_user u on u.id_role=r.id\n" +
                    "where p.permission_value='"+role+"'\n" +
                    "and u.id='"+userid+"'";
        return mr.mapList(query);
        
    }
    public List<Map<String, Object>> statusAppv(String username, String jenis, Integer level){
        String query="select * from fn_auth_status('"+username+"', '"+jenis+"', "+level+") as (level int, id_jabatan int)'";
        return mr.mapList(query);
        
    }
    
    public List<Map<String, Object>> listUnorUser(String username){
        String query="SELECT \n" +
        "  m_organisasi.id_pegawai, \n" +
        "  c_security_user.username, \n" +
        "  m_pegawai.nama AS nama_pegawai, \n" +
        "  m_pegawai.nip, \n" +
        "  m_unor.id AS id_unor, \n" +
        "  m_unor.nama AS nama_unor, \n" +
        "  m_jabatan.nama AS jabatan\n" +
        "FROM \n" +
        "  public.c_security_user, \n" +
        "  public.m_pegawai, \n" +
        "  public.m_organisasi, \n" +
        "  public.m_unor, \n" +
        "  public.m_jabatan\n" +
        "WHERE \n" +
        "  c_security_user.id_pegawai = m_pegawai.id AND\n" +
        "  m_organisasi.id_unor = m_unor.id AND\n" +
        "  m_organisasi.id_pegawai = m_pegawai.id AND\n" +
        "  m_organisasi.id_jabatan = m_jabatan.id AND\n"+
        "  c_security_user.username='"+username+"' order by public.m_unor.nama;";
        return mr.mapList(query);
        
    }
    
}
