package crawer.cyy.cnooc;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

import com.yinkai.pojo.Docbaseinternetworm;
import com.yinkai.service.DocbaseinternetwormService;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * 网站爬虫
 * https://buy.cnooc.com.cn/cbjyweb/001/moreinfo.html
 *
 * 默认50条线程，间隔1秒
 *
 * 将数据保存到 docbaseinternetworm 表中，
 * 增加了 url和md5  2个字段
 * 请查看 docbaseinternetworm 表是否缺少字段，并根据需要执行以下sql
 *
ALTER TABLE [dbo].[docbaseinternetworm] ADD [url] varchar(255) NULL
GO

ALTER TABLE [dbo].[docbaseinternetworm] ADD [md5] varchar(32) NULL
GO
 *
 * @author xh
 * @date 18-04-02 002
 */
public class CnoocCrawerService extends BreadthCrawler {

    private static final String SOURCE = "cnooc";
    private static final String HOME_URL = "https://buy.cnooc.com.cn";
    private static final String URL = "https://buy.cnooc.com.cn/cbjyweb/001/*.html";

    private static final String LIST_TYPE = "list";
    private static final String CONTENT_TYPE = "content";


    public CnoocCrawerService(int start,int end) {
        super("cnoocCrawer", false);

        for (int i = start; i <= end; i++) {
            this.addSeed(URL.replace("*", String.valueOf(i)), LIST_TYPE);
        }

        //设置线程数量
        setThreads(50);
        setExecuteInterval(1000);
    }



    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        String url = page.url();
        try {
            if (page.matchType(LIST_TYPE)) {
                for (Element element : page.select("#categorypagingcontent ul li")) {
                    String href = HOME_URL+element.select("a").attr("href");
                    String date = element.select("span").text();
                    /*
                    * 在详情页，有的文章没有日期，因此日期在列表页中获取。
                    * 这里使用meta()方法传递参数，参数名为date
                    * */
                    crawlDatums.add(new CrawlDatum(href,CONTENT_TYPE).meta("date",date));
                }
            } else if (page.matchType(CONTENT_TYPE)) {
                String title = page.select(".article-title").text();
                String content = page.select(".article-Content").html();
                /*
                * 获取date参数
                * */
                String date = page.meta("date");

                Docbaseinternetworm docbaseinternetworm = new Docbaseinternetworm();
                docbaseinternetworm.setTitle(title);
                docbaseinternetworm.setContent(content);
                docbaseinternetworm.setDate(date);
                docbaseinternetworm.setSource(SOURCE);
                docbaseinternetworm.setUrl(url);

                DocbaseinternetwormService.getInstance().addOrUpdate(docbaseinternetworm);

            } else {
                System.out.println("other");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("有异常");
        }
    }

    public void execute(){
        try {
            start(2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提取《xxx/xxx/abcd.html》中子abcd的值，abcd共36位
     * @param url xxx/xxx/abcd.html
     * @return abcd
     */
    private String selectId(String url) {
        int endIndex = url.lastIndexOf(".");
        int startIndex = endIndex - 36;
        return url.substring(startIndex, endIndex);
    }


}
