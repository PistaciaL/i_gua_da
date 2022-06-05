package org.nwpu.i_gua_da.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java email validation program
 *
 * @author pankaj
 *
 */
public class FormatValidator {

    public static boolean isEmail(String email) {

        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher e = p.matcher(email);
        return e.matches();
    }
}