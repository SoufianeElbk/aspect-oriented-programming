package aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SecondAspect {
    @Pointcut("execution(* Application.mai(..))")
    public void pc1(){}
    @Before("pc1()")
    public void beforeMain(){
        System.out.println("Before starting Main @Aspect Syntax ... ");
    }
    @After("pc1()")
    public void afterMain(){
        System.out.println("After starting Main @Aspect Syntax ... ");
    }
}