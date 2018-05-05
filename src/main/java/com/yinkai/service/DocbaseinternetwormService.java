package com.yinkai.service;

import com.yinkai.dao.BaseDAO;
import com.yinkai.pojo.Docbaseinternetworm;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Map;

public class DocbaseinternetwormService extends BaseDAO<Docbaseinternetworm> {

    // 测试 wordFormat4DB 正常运行的情况
    @Test
    public void wordFormat4DBNormal(){
        DocbaseinternetwormService docbaseinternetwormService = new DocbaseinternetwormService();
        try {
            Map<String, Object> oneMap = docbaseinternetwormService.findOneMap("select * from docbaseinternetworm where id=?", new String[]{"1"});

            System.out.println(oneMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}


