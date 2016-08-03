package com.zxy.books.network.parser;

import android.content.Context;

import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * html解析类
 *
 * @author xc
 */
public class GokankanParser {
    private String searchUrl = "http://www.7kankan.com/modules/article/sosearch.php?searchtype=articlename&searchkey=";// &page=3
    // <em id="pagestats">3/3</em> 搜索结果的总页数
    private String parseSearch = "table.grid tr";// jsoup解析搜索的结果html页面,找到所有书籍内容的检索条件,要去掉第一行的目录
    private List<Book> bookList = new ArrayList<Book>();

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    /**
     * 书籍查找
     *
     * @param result
     * @param context
     * @return
     */
    public List<Book> getSearchBookList(String result, Context context) {
        Document doc;
        bookList.clear();
        if (!result.equals("")) {// 得到html结果
            try {
                doc = Jsoup.parse(result);
                Element ePageCount = doc.select("em#pagestats").first(); // 找到结果总共有几页;
                String strPageCount = ePageCount.text();
                strPageCount = strPageCount.substring(
                        strPageCount.length() - 1, strPageCount.length());
                Elements es = doc.select(parseSearch);
                for (int i = 1; i < es.size(); i++) {// 第一行不要是标题;
                    Element e = es.get(i);// tr行
                    Elements etd = e.select("td");// 每个单元格共有六格
                    Element e1 = etd.get(0).child(0);// 书名和简介链接
                    String breifLink = e1.attr("href");
                    String title = e1.text();// 书名
                    Element e2 = etd.get(1).child(0);// 最新章节内容和导引目录链接
                    String chapterUrl = "http://www.7kankan.com/"
                            + e2.attr("href");
                    String lastChapterContent = e2.text();
                    Element e3 = etd.get(2);// 作者名称
                    String authorName = e3.text();
                    Element e4 = etd.get(3);// 文件大小
                    String fileSize = e4.text();
                    Element e5 = etd.get(4);// 最后更新时间
                    String lastUpdateTime = e5.text();
                    Element e6 = etd.get(5);// 小说类型 连载或者完本
                    String finish = e6.text();
                    Book bk = new Book();
                    String str[] = breifLink.split("/");
                    bk.setType(str[str.length - 2]);
                    String str1 = str[str.length - 1].substring(0, str[str.length - 1].length() - 4);
                    bk.setLastChapter(lastChapterContent);
                    bk.setBookId(str1);
                    bk.setBookName(title);
                    bk.setIndexUrl(breifLink);
                    bk.setBookAuthor(authorName);
                    bk.setChapterUrl(chapterUrl);
                    bk.setChapterUrlHead(chapterUrl.replace("/index.html", ""));
                    bk.setUpdataTime(lastUpdateTime);
                    bk.setFinnish(finish);
                    /*
                     * bk.setSiteId(Integer.parseInt("115")); bk.setSiteId(115);
					 */
                    bookList.add(bk);
                }// end for
                // if(intPageCount>1)//找到结果不止一页
                // {
                // getMorePages(context,intPageCount);
                // }
            }// end try
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bookList;
    }

    /**
     * 根据书籍的目录url地址html内容，解析成一个章节数组
     *
     * @param result html内容
     * @param bk     书籍集合
     * @return
     */
    public List<BookChapter> getChaptersByIndexUrl(String result, Book bk) {
        int pos = bk.getChapterUrl().lastIndexOf("/");
        String preUrl = bk.getChapterUrl().substring(0, pos + 1);
        List<BookChapter> listChapters = new ArrayList<BookChapter>();
        int i = 0;// 非vip章节的编号,跟数组同步从0开始
        Document doc;
        try {
            doc = Jsoup.parse(result);
            Elements es = doc.select("div.uclist>dl>dd>a");
            if (!es.isEmpty()) {
                for (Element e : es) {
                    String chaperlink = e.attr("href");
                    String chapterName = e.text();// 找到的章节名
                    BookChapter chapter = new BookChapter();
                    chapter.setBookId(bk.getBookId());
                    chapter.setChapterNo(i);// 章节编号还是用数字表示第几章节
                    chapter.setChapterTitle(chapterName);
                    chapter.setChapterUrl(preUrl + chaperlink);
                    chapter.setChapterId(chaperlink.replace(".html", ""));
                    if (listChapters.size() != 0) {//如果章节列表不为空
                        chapter.setLastChapter(listChapters.get(listChapters.size() - 1));//将上一个章节给本章节LastChapter
                        listChapters.get(listChapters.size() - 1).setNextChapter(chapter);//将本章节给上一个章节的NextChapter
                    }
                    listChapters.add(chapter);
                    i++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listChapters;
    }

    /**
     * 书籍详细信息
     *
     * @param result
     * @param bk
     */
    public void getBookDetail(String result, Book bk) {
        // MyHttpClient myClient=new MyHttpClient();
        // String result=myClient.getHtml(bk.getIndexUrl());
        Document doc;
        if (!result.equals("")) {// 得到html结果
            try {
                doc = Jsoup.parse(result);
                Element eImg = doc.select("span.hottext").get(0)
                        .previousElementSibling();
                String imgUrl = eImg.attr("href");
                //  bk.setBookOnlineImage(imgUrl);
                Element eDescription = doc.select("span.hottext").first()
                        .parent();
                String tdString = eDescription.outerHtml();
                String strTemp = "<span class=\"hottext\">内容简介：</span><br />";
                int iPos = tdString.indexOf(strTemp);
                tdString = tdString.substring(iPos + 1 + strTemp.length());
                tdString = tdString.replace("&nbsp;", "");
                tdString = tdString.replace("nbsp;", "");
                tdString = tdString.replace("</td>", "");
                tdString = tdString.replace("<br />", "\n");
                bk.setBookDesc(tdString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 书籍内容解析
     *
     * @param result
     * @param bookChapter
     */
    public void readbook(String result, BookChapter bookChapter) {
        if (!result.equals("")) {
            Document doc;
            try {
                doc = Jsoup.parse(result);
                Elements es = doc.select("#content");
                if (!es.isEmpty()) {
                    String tdString = es.html();
                    tdString = tdString.replace("&nbsp;", "");
                    tdString = tdString.replace("nbsp;", "");
                    tdString = tdString.replace("</td>", "");
                    tdString = tdString.replace("<br />", " ");
                    bookChapter.setChapterContext(tdString);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}