package com.github.rzabini.org.approvaltests.spock;

import org.junit.Test;
import java.util.Arrays;


public class SampleArrayTest
{
  @Test
  public void testListJunit() throws ClassNotFoundException {
    String[] names = {"Llewellyn", "James", "Dan", "Jason", "Katrina"};
    Arrays.sort(names);
    Class.forName(SpockApprovals.class.getName());
    SpockApprovals.verifyAll("", names);
  }
}
