package com.vk.dal;

import com.vk.dal.config.MySQLAutoconfiguration;
import com.vk.dal.domain.Customer;
import com.vk.dal.repository.CustomerRepository;
import org.jboss.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Application {
  private static ApplicationContext applicationContext;
  private static final Logger logger = Logger.getLogger(Application.class);

  static CustomerRepository customerRepository;
  static DataSource dataSource;

  public static void main(String[] args) throws InterruptedException {

    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MySQLAutoconfiguration.class);

    customerRepository = ctx.getBean(CustomerRepository.class);
    dataSource = ctx.getBean(DataSource.class);

    testConnectionCharacteristics();
    runTest();
  }

  private static void testConnectionCharacteristics() throws InterruptedException {
    long opsStartTime = System.nanoTime();

    try (Connection conn = dataSource.getConnection()) {
      long opsStopTime = System.nanoTime();
      System.out.println("Connection establish time(millis): " + TimeUnit.NANOSECONDS.toMillis(opsStopTime - opsStartTime));

      try (Statement testStmt = conn.createStatement()) {
        long stmtTestStart = System.nanoTime();
        final String testStatement = "select 1";

        testStmt.executeQuery(testStatement).close();
        long stmtTestEnd = System.nanoTime();
        System.out.format("Test statement '%s' exec time(millis): %d%n", testStatement, TimeUnit.NANOSECONDS.toMillis(stmtTestEnd - stmtTestStart));
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
      throw new InterruptedException(ex.getMessage());
    }

  }

  private static void runTest() throws InterruptedException {


    long startTime = System.nanoTime();


    Customer customerJohn = customerRepository.save(new Customer("John"));
    long spentTime = System.nanoTime() - startTime;
    System.out.format("Time spent to save one entity: %d; Object id(pk): %d%n", TimeUnit.NANOSECONDS.toMillis(spentTime), customerJohn.getId());


    startTime = System.nanoTime();
    Customer customerAlice = customerRepository.save(new Customer("Alice"));
    spentTime = System.nanoTime() - startTime;
    System.out.format("Time spent to save 2nd entity: %d; Object id(pk): %d%n", TimeUnit.NANOSECONDS.toMillis(spentTime), customerAlice.getId());


    Customer foundEntity = customerRepository.findOne(customerJohn.getId());

    System.out.println("\n1.findAll()...");
    startTime = System.nanoTime();
    List<Customer> customers = customerRepository.findAll();
    spentTime = System.nanoTime() - startTime;
    for (Customer customer : customers) {
      System.out.println(customer.getName());
    }
    System.out.println("Time spent to find all entities: " + TimeUnit.NANOSECONDS.toMillis(spentTime));

    Thread.sleep(3000);

    startTime = System.nanoTime();
    customers = customerRepository.findAll();
    spentTime = System.nanoTime() - startTime;

    System.out.println("Time spent to find all entities 2nd time: " + TimeUnit.NANOSECONDS.toMillis(spentTime));

    System.out.println("Done!");

//    exit(0);
  }
}