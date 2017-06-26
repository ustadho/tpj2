/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author faheem
 */
@Repository("mapResultSetRepository")
@Transactional
public class MapResultSet {

    @Autowired
    DataSource dataSource;

    org.slf4j.Logger logger = LoggerFactory.getLogger(MapResultSet.class);

    public Map<String, Object> mapSingle(String sql) {
        Map<String, Object> hasil = new HashMap<String, Object>();

        try {
            Connection conn = dataSource.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ResultSetMetaData rm = rs.getMetaData();
            if (rs.next()) {
                for (int i = 0; i < rm.getColumnCount(); i++) {
                    hasil.put(rm.getColumnName(i + 1), rs.getObject(i + 1));
                }
            }
            rs.close();
            conn.close();
            rs = null;
            conn = null;
        } catch (SQLException ex) {
            Logger.getLogger(MapResultSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public List<Map<String, Object>> mapList(String sql) {
        logger.warn(sql);
        List<Map<String, Object>> hasil = new ArrayList<Map<String, Object>>();

        try {
            Connection conn = dataSource.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ResultSetMetaData rm = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<String, Object>();
                for (int i = 0; i < rm.getColumnCount(); i++) {
                    row.put(rm.getColumnName(i + 1), rs.getObject(i + 1));
                }
                hasil.add(row);
            }
            rs.close();
            conn.close();
            rs = null;
            conn = null;
        } catch (SQLException ex) {
            Logger.getLogger(MapResultSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public Connection getConn() {
        Connection cn = null;
        try {
            cn = dataSource.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MapResultSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cn;
    }

    Integer countRecordset(String sql) {
        Integer i = new Integer(0);
        try {
            Connection conn = dataSource.getConnection();
            ResultSet rs = conn
                    .createStatement()
                    .executeQuery("select count(*) from (" + sql + ")as x");
            if (rs.next()) {
                i = rs.getInt(1);
            }

            rs.close();
            conn.close();
            rs = null;
            conn = null;

        } catch (SQLException ex) {
            logger.warn("Error [{}]", ex);
            return 0;
        }
        return i;
    }
}
