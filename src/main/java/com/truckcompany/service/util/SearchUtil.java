package com.truckcompany.service.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class SearchUtil {

    public static Collection<String> splitSearchTermAndRemoveIgnoredCharacters(String searchTerm, Pattern pattern) {
        String[] searchTerms = StringUtils.split(searchTerm, " ");
        List<String> result = new ArrayList<String>(searchTerms.length);
        for (String term : searchTerms) {
            if (StringUtils.isNotEmpty(term)) {
                result.add(pattern.matcher(term).replaceAll(" "));
            }
        }
        return result;
    }
}
