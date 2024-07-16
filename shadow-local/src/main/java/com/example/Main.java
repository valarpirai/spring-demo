package com.example;


import org.joda.time.LocalDate;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  void test() {
    System.out.println("Time " + java.time.Instant.now());
    System.out.println("Time " + LocalDate.now());
    System.out.println("Time " + org.joda.time.Instant.now());
  }
}
