/*
 * AllTests.java
 * copyright (c) 2014 by Alexander Lehmann <afwlehmann@googlemail.com>
 */

package jiter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
      ListUnitTests.class,
      IterateeUnitTests.class,
      IterateesUnitTests.class
})
public class AllTests {}
