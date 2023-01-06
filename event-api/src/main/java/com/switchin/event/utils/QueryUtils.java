package com.switchin.event.utils;

public class QueryUtils {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 10;

    private QueryUtils() {

    }


    public static int getPage(String page) {
        return (page == null)
                ? DEFAULT_PAGE
                : Math.max(Integer.parseInt(page), DEFAULT_PAGE);
    }


    public static int getLimit(String limit) {
        return (limit == null)
                ? DEFAULT_LIMIT
                : Math.min(Integer.parseInt(limit), DEFAULT_LIMIT);
    }


    public static int getOffset(int page,
                                int limit) {
        if ((page - 1) * limit >= 0) {
            return (page - 1) * limit;
        } else {
            throw new NumberFormatException(LogUtils.NULL_OFFSET_ERROR_MESSAGE.buildMessage(page, limit));
        }
    }

}
