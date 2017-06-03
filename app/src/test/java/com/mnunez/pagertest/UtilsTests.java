package com.mnunez.pagertest;

import com.mnunez.pagertest.utils.DateUtils;
import com.mnunez.pagertest.utils.StringUtils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UtilsTests {

    @Test
    public void testStringUtils_isEmpty() throws Exception {
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isNotEmpty("Not Empty"));
    }

    @Test
    public void testStringUtils_isNotEmpty() throws Exception {
        assertTrue(StringUtils.isNotEmpty("Not Empty"));
    }

    @Test
    public void testStringUtils_nullTrim() throws Exception {
        assertFalse(StringUtils.nullTrim(" Test ").contains(" "));
    }

    @Test
    public void testDateUtils_getStringFromDate() throws Exception {
        Date date = DateUtils.getDateFromString("01/01/2017", "MM/dd/yyyy");
        String s_date = DateUtils.getStringFromDate(date, "MM/dd/yyyy");
        assertTrue(s_date.equals("01/01/2017"));
    }

    @Test
    public void testDateUtils_getStringFromDateWhenNull() throws Exception {
        String s_date = DateUtils.getStringFromDate(null, "MM/dd/yyyy");
        assertTrue(StringUtils.isEmpty(s_date));
    }

}
