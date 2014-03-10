/*
 * Copyright 2014 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitblit.plugins.javamelody;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bull.javamelody.MonitoringFilter;

import com.gitblit.extension.AllRequestFilter;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public class GitblitJavamelodyPlugin extends Plugin {

  public GitblitJavamelodyPlugin(PluginWrapper wrapper) {
    super(wrapper);
  }

  @Override
  public void start() {
    // no op
  }

  @Override
  public void stop() {
    // no op
  }

  @Extension
  public static class GitblitMonitoringFilter extends AllRequestFilter {
    private final JavamelodyFilter monitoring = new JavamelodyFilter();

    @Override
    public void init(FilterConfig config) throws ServletException {
      monitoring.init(config);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
      if (!(request instanceof HttpServletRequest)
          || !(response instanceof HttpServletResponse)) {
        chain.doFilter(request, response);
        return;
      }

      HttpServletResponse httpResponse = (HttpServletResponse) response;
      HttpServletRequest httpRequest = (HttpServletRequest) request;

      if (canMonitor(httpRequest)) {
        monitoring.doFilter(request, response, chain);
      } else {
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
            "Forbidden access");
      }
    }

    @Override
    public void destroy() {
      monitoring.destroy();
    }

    private boolean canMonitor(HttpServletRequest httpRequest) {
      if (httpRequest.getRequestURI().equals(monitoring
          .getJavamelodyUrl(httpRequest))) {
        // TODO(davido):Check if current user has Administrator role
        // access GitBlit's user manager and perform check.
        return true;
      }
      return true;
    }
  }

  static class JavamelodyFilter extends MonitoringFilter {
    public String getJavamelodyUrl(HttpServletRequest httpRequest) {
      return getMonitoringUrl(httpRequest);
    }
  }
}
