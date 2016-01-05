/*
 * SonarQube :: Batch
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.batch.mediumtest.log;

import java.util.Collections;

import org.hamcrest.Matchers;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.sonar.batch.bootstrapper.EnvironmentInformation;
import org.sonar.api.utils.MessageException;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.sonar.batch.protocol.input.GlobalRepositories;
import org.sonar.batch.repository.GlobalRepositoriesLoader;
import org.sonar.batch.bootstrapper.Batch;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExceptionHandlingMediumTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Batch batch;
  private static ErrorGlobalRepositoriesLoader loader;

  @BeforeClass
  public static void beforeClass() {
    loader = new ErrorGlobalRepositoriesLoader();
  }

  public void setUp(boolean verbose) {
    Batch.Builder builder = Batch.builder()
      .setEnableLoggingConfiguration(true)
      .addComponents(
        loader,
        new EnvironmentInformation("mediumTest", "1.0"));

    if (verbose) {
      builder.setBootstrapProperties(Collections.singletonMap("sonar.verbose", "true"));
    }
    batch = builder.build();
  }

  @Test
  public void test() throws Exception {
    setUp(false);
    loader.withCause = false;
    thrown.expect(MessageException.class);
    thrown.expectMessage("Error loading repository");
    thrown.expectCause(Matchers.nullValue(Throwable.class));

    batch.start();
  }

  @Test
  public void testWithCause() throws Exception {
    setUp(false);
    loader.withCause = true;

    thrown.expect(MessageException.class);
    thrown.expectMessage("Error loading repository");
    thrown.expectCause(new TypeSafeMatcher<Throwable>() {
      @Override
      public void describeTo(Description description) {
      }

      @Override
      protected boolean matchesSafely(Throwable item) {
        return item instanceof IllegalStateException && item.getMessage().equals("Code 401");
      }
    });

    batch.start();
  }

  @Test
  public void testWithVerbose() throws Exception {
    setUp(true);
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Unable to load component class");
    batch.start();
  }

  private static class ErrorGlobalRepositoriesLoader implements GlobalRepositoriesLoader {
    boolean withCause = false;

    @Override
    public GlobalRepositories load(MutableBoolean fromCache) {
      if (withCause) {
        IllegalStateException cause = new IllegalStateException("Code 401");
        throw MessageException.of("Error loading repository", cause);
      } else {
        throw MessageException.of("Error loading repository");
      }
    }
  }
}
