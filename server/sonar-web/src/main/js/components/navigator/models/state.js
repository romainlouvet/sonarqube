/*
 * SonarQube :: Web
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
import _ from 'underscore';
import Backbone from 'backbone';

export default Backbone.Model.extend({
  defaults: function () {
    return {
      page: 1,
      maxResultsReached: false,
      query: {},
      facets: []
    };
  },

  nextPage: function () {
    var page = this.get('page');
    this.set({ page: page + 1 });
  },

  clearQuery: function (query) {
    var q = {};
    Object.keys(query).forEach(function (key) {
      if (query[key]) {
        q[key] = query[key];
      }
    });
    return q;
  },

  _areQueriesEqual: function (a, b) {
    var equal = Object.keys(a).length === Object.keys(b).length;
    Object.keys(a).forEach(function (key) {
      equal = equal && a[key] === b[key];
    });
    return equal;
  },

  updateFilter: function (obj, options) {
    var oldQuery = this.get('query'),
        query = _.extend({}, oldQuery, obj),
        opts = _.defaults(options || {}, { force: false });
    query = this.clearQuery(query);
    if (opts.force || !this._areQueriesEqual(oldQuery, query)) {
      this.setQuery(query);
    }
  },

  setQuery: function (query) {
    this.set({ query: query }, { silent: true });
    this.set({ changed: true });
    this.trigger('change:query');
  }
});


