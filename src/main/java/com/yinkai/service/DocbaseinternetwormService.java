package com.yinkai.service;

import com.yinkai.dao.BaseDAO;
import com.yinkai.pojo.Docbaseinternetworm;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class DocbaseinternetwormService extends BaseDAO<Docbaseinternetworm> {

    private static DocbaseinternetwormService docbaseinternetwormService = new DocbaseinternetwormService();

    public DocbaseinternetwormService() {}
    public static DocbaseinternetwormService getInstance(){
        return docbaseinternetwormService;
    }

    public int add(Docbaseinternetworm docbaseinternetworm) throws SQLException {
        //生成去横杆的uuid当做爬取文章的id
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        ArrayList<Object> pram = new ArrayList<Object>();

        pram.add(id);
        pram.add(docbaseinternetworm.getTitle());
        pram.add(docbaseinternetworm.getContent());
        pram.add(docbaseinternetworm.getDate());
        pram.add(docbaseinternetworm.getAuthor());
        pram.add(docbaseinternetworm.getViews());
        pram.add(docbaseinternetworm.getSource());
        int i = insert1("INSERT INTO docbaseinternetworm (id, title, content, date, author, views, source) VALUES (?, ?, ?, ?, ?, ?, ?)", pram.toArray());
        String title = (String) findOneCol("select title from docbaseinternetworm where title=?", new Object[]{docbaseinternetworm.getTitle()}, "title");
        System.out.println("========title===>"+title);


        return i;
}



    /**
     * 更新操作
     * <p>
     * 注意点：在进行爬虫作业时，会产生数据库死锁
     * 加入synchronized后未见死锁，具体原因未知
     *

     */
    public synchronized int update(Docbaseinternetworm docbaseinternetworm) throws SQLException {
        ArrayList<Object> pram = new ArrayList<Object>();
        pram.add(docbaseinternetworm.getTitle());
        pram.add(docbaseinternetworm.getContent());
        pram.add(docbaseinternetworm.getDate());
        pram.add(docbaseinternetworm.getSource());
        pram.add(docbaseinternetworm.getMd5());
        pram.add(docbaseinternetworm.getId());
        final String sql = "update docbaseinternetworm set title=?,content=?,date=?,source=?,md5=? where id=?";

        return update(sql,pram.toArray());
    }

    /**
     * 插入或更新
     * 使用url作为唯一标识，只有找不到相同的url时，才进行插入操作
     * <p>
     * 使用标题、内容和日期三者的md5值作为更新的判断，只有值不同时，才进行更新操作
     *

     * @return 数据库影响数据数量
     * @author xh
     * @date 18-04-04
     */
    public int addOrUpdate(Docbaseinternetworm docbaseinternetworm) throws SQLException {

        final String findSql = "select id,md5 from docbaseinternetworm where url = ?";
        Map<String, Object> map = findOneMap(findSql,new Object[]{docbaseinternetworm.getUrl()});

        String md5 = getMd5(docbaseinternetworm.getTitle()+docbaseinternetworm.getContent()+docbaseinternetworm.getDate());

        //如果存在则更新
        if (map != null &&map.get(0)!=null&&"".equals(map.get(0))) {
            String dbMd5 = String.valueOf(map.get("md5"));

            if (!md5.equals(dbMd5)) {
                //内容改变则更新！
                String id = String.valueOf(map.get("id"));
                return update(docbaseinternetworm);
            } else {
                return 0;
            }
        }
        //保存在则插入
        else {
            return add(docbaseinternetworm);
        }
    }


    public static String getMd5(String source) {

        StringBuffer sb = new StringBuffer(32);

        try {
            MessageDigest md    = MessageDigest.getInstance("MD5");
            byte[] array        = md.digest(source.getBytes("utf-8"));

            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            return null;
        }

        return sb.toString();
    }

}


