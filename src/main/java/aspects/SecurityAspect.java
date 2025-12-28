package aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Scanner;

@Aspect
public class SecurityAspect {
    @Pointcut("call(* Application.start(..))")
    public void startAppPointcut(){}

    @Around("startAppPointcut()")
    public void autoStart(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Security Check");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!"admin".equals(username) || !"admin".equals(password)) {
            System.out.println("Authentication failed! Access denied.");
            return; // Block the method execution
        }
        pjp.proceed();
    }
}