/*
 * SonarQube :: Plugin API
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
package org.sonar.api.issue;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @since 3.6
 */
public interface DefaultTransitions {

  String CONFIRM = "confirm";
  String UNCONFIRM = "unconfirm";
  String REOPEN = "reopen";
  String RESOLVE = "resolve";
  String FALSE_POSITIVE = "falsepositive";
  String CLOSE = "close";

  /**
   * @since 5.1
   */
  String WONT_FIX = "wontfix";

  /**
   * @since 4.4
   */
  List<String> ALL = ImmutableList.of(CONFIRM, UNCONFIRM, REOPEN, RESOLVE, FALSE_POSITIVE, WONT_FIX, CLOSE);
}
