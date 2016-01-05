/*
 * SonarQube :: Database
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
package org.sonar.core.issue;

import java.util.Date;
import org.sonar.api.issue.ActionPlan;
import org.sonar.core.util.Uuids;

public class ActionPlanStats extends DefaultActionPlan {

  private int totalIssues;
  private int unresolvedIssues;

  private ActionPlanStats() {

  }

  public static ActionPlanStats create(String name) {
    ActionPlanStats actionPlan = new ActionPlanStats();
    actionPlan.setKey(Uuids.create());
    Date now = new Date();
    actionPlan.setName(name);
    actionPlan.setStatus(ActionPlan.STATUS_OPEN);
    actionPlan.setCreatedAt(now).setUpdatedAt(now);
    return actionPlan;
  }

  public int totalIssues() {
    return totalIssues;
  }

  public ActionPlanStats setTotalIssues(int totalIssues) {
    this.totalIssues = totalIssues;
    return this;
  }

  public int unresolvedIssues() {
    return unresolvedIssues;
  }

  public ActionPlanStats setUnresolvedIssues(int unresolvedIssues) {
    this.unresolvedIssues = unresolvedIssues;
    return this;
  }

  public int resolvedIssues() {
    return totalIssues - unresolvedIssues;
  }

  public boolean isOpen() {
    return ActionPlan.STATUS_OPEN.equals(status());
  }

  public boolean overDue() {
    Date deadline = deadLine();
    return isOpen() && deadline != null && new Date().after(deadline);
  }
}
