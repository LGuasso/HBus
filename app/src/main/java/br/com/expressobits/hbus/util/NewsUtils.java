package br.com.expressobits.hbus.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.utils.TextUtils;

/**
 * Created by rafael on 21/08/16.
 */
public class NewsUtils {

    public static List<News> getNews(){
        ArrayList<News> newses = new ArrayList<>();
        News news = new News();
        news.setTitle("Meu Deus do Céu!");
        news.setBody("Lsdasdhasjd sdhasjkdhashjkdhaskhd sdhsajdhkashkd akshdkuashdkjashdkjahs dashdkjahsdkjhaskd" +
                "sjdsakjdkajskdnnk diashdkjahshsajkdh sahdjashdjhajsdhjas djashdjashjdhjashjdhsajskllllll sajd sajdsaiwjep !");
        news.setTime(System.currentTimeMillis());
        news.setSource("http://www.youtube.com");
        news.setImagesUrls(Arrays.asList(new String[]{"sda"}));
        news.setId("/meu_deus_do_ceu");
        newses.add(news);
        return newses;
    }
}
