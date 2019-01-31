package com.example.newsappxmlrss;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Deep1 on 2/26/2018.
 */

public class ArticleParser {

    public static class ArticlePullParser {
        static public ArrayList<Article> parseArticle(InputStream inputStream) throws XmlPullParserException, IOException {
            ArrayList<Article> articles = new ArrayList<>();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            int event = parser.getEventType();
            Article article = null;
            boolean flag = false;
            boolean double_flag = false;
            int counter = 0;
            while(event!=XmlPullParser.END_DOCUMENT) {
                switch (event){
                    case XmlPullParser.START_TAG:
                        //Log.d("article","Tag Name : " + parser.getName());
                        if(counter == 34) {
                            Log.d("article","Tag Name : " + parser.getName());
                        }
                        if(parser.getName().equals("item")) {
                            flag = true;
                            article = new Article();
                        }
                        else if(parser.getName().equals("title") && flag) {
                            article.title = parser.nextText().trim();
                        }
                        else if(parser.getName().equals("pubDate") && flag) {
                            article.publishedAt = parser.nextText().trim();
                        }
                        else if(parser.getName().equals("description") && flag) {
                            String desc = parser.nextText().trim();
                            desc = desc.substring(0,desc.indexOf("<div class=\"feedflare\">"));
                            Log.d("article","desc => " + desc);
                            article.description = desc;
                        }
                        else if(parser.getName().equals("link") && flag) {
                            String desc = parser.nextText().trim();
                            //desc = desc.substring(0,desc.indexOf("<div class=\"feedflare\">"));
                            Log.d("url","Link => " + desc);
                            article.link = desc;
                        }
                        else if(parser.getName().equals("media:content") && (flag || double_flag)) {
                            String urlToimage = parser.getAttributeValue(null,"url");
                            if (urlToimage == null) {
                                double_flag = true;
                            } else {
                                article.urlToImage = urlToimage;
                            }
                            flag = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            Log.d("article","End Tag");
                            articles.add(article);
                            counter += 1;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
            Log.d("article",articles.size()+"");
            return articles;
        }
    }
}