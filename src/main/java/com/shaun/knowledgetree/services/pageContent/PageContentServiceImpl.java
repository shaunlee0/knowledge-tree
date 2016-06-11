package com.shaun.knowledgetree.services.pageContent;

import com.shaun.knowledgetree.domain.Category;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shaun on 29/05/2016.
 */
public class PageContentServiceImpl implements PageContentService {

    @Override
    public Set<Category> extractCategories(String pageText) {
        Set<Category> toReturn = new HashSet<>();
        String[] categories = pageText.split("Category:");
        for (int i = 1; i < categories.length; i++) {
            categories[i] = categories[i].replace("]]\n[[", "");
            int indexOfEndOfCategory = categories[i].indexOf("|");
            if (indexOfEndOfCategory != -1) {
                categories[i] = categories[i].substring(0, indexOfEndOfCategory);
            }
            toReturn.add(new Category(categories[i]));
        }
        return toReturn;
    }

    @Override
    public Map<String, String> extractKeyValuePairs(String content) {
        Map<String,String> map = new HashMap<>();
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] keys = lines[i].split("= ");
            if(keys.length > 1){

                keys[0] = keys[0].replace("|","").trim();
                //Remove html tags
                keys[1] = Jsoup.parse(keys[1]).text();

//                //Remove confusing alternate term for brevity
//                int index = keys[1].indexOf("|");
//                if(index!=-1){
//                    keys[1] = keys[1].substring(0,index);
//                }

                //If either key or value is under 70 length we deem it valid and add it to map
                if(keys[0].length() < 70 || keys[1].length() < 70){
                    map.put(keys[0],keys[1]);
                }
            }
        }

        return map;
    }
}
