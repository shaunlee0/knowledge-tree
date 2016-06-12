package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.Link;

import java.util.*;

/**
 * Util class for a singular Wiki Entity, for processes such as sorting etc...
 */
public class WikiEntityUtil {


    public static List<Link> sortByComparator(List<Link> links) {

        Collections.sort(links, (left, right) -> left.getScore() - right.getScore());

        return links;
    }

}
