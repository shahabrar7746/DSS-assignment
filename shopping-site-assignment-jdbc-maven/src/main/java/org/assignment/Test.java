package org.assignment;

import org.assignment.services.AdminService;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, FileNotFoundException {
       AdminService service = AdminService.getInstance();
        System.out.println(service);
//        Logger logger = LogManager.getLogger(Test.class);
//        Appender appender = logger.getAppender("src/resources/log4j2.xml");
//        logger.info(System.getenv("SUPER_ADMIN_PASSWORD"));

    }
}
