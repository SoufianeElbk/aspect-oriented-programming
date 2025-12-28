package aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {
    Logger logger = Logger.getLogger(LoggingAspect.class.getName());
    long t1,t2;

    LoggingAspect() throws IOException {
        logger.addHandler(new FileHandler("log.xml"));
        logger.setUseParentHandlers(false);
    }

    @Pointcut("execution(* metier.IMetierImpl.*(..))")
    public void pc1(){}

    @Before("pc1()")
    public void before(JoinPoint joinPoint) {
        t1 = System.currentTimeMillis();
        logger.info("--------------------------------------------------------------------");
        logger.info(joinPoint.getSignature() + " is about to be executed.");
    }

    @After("pc1()")
    public void after(JoinPoint joinPoint) {
        logger.info(joinPoint.getSignature() + " has just been executed.");
        t2 = System.currentTimeMillis();
        logger.info("Execution time: " + (t2 - t1) + " ms");
        logger.info("--------------------------------------------------------------------");
    }
}
