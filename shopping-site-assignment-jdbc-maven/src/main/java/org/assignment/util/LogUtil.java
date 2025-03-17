package org.assignment.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static final Logger logger = LogManager.getLogger(LogUtil.class);
    public static Response logError(Object object){
        logger.error(object);
        return new Response(null, "Some error occured");
    }
}
