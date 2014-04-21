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
package com.gitblit.plugin.javamelody;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bull.javamelody.MonitoringFilter;
import ro.fortsoft.pf4j.Extension;

import com.gitblit.extensions.HttpRequestFilter;
import com.gitblit.manager.IAuthenticationManager;
import com.gitblit.models.UserModel;
import com.gitblit.servlet.GitblitContext;

/**
 * Request filter that delegates to JavaMelody and authorizes access to the
 * JavaMelody UI.
 *
 * @author David Ostrovsky
 *
 */
@Extension
public class GitblitMonitoringFilter extends HttpRequestFilter {

	/* Subclass to elevate the scope of getMonitoringUrl() */
	static class JavamelodyFilter extends MonitoringFilter {
		public String getJavamelodyUrl(HttpServletRequest httpRequest) {
			return getMonitoringUrl(httpRequest);
		}
	}

	private final JavamelodyFilter monitoring = new JavamelodyFilter();

	@Override
	public void init(FilterConfig config) throws ServletException {
		Plugin.instance().setConfig(config);
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
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden access");
		}
	}

	@Override
	public void destroy() {
		monitoring.destroy();
	}

	private boolean canMonitor(HttpServletRequest httpRequest) {
		if (httpRequest.getRequestURI().equals(monitoring.getJavamelodyUrl(httpRequest))) {
			// request is for the JavaMelody app, authorize admins
			IAuthenticationManager authManager = GitblitContext.getManager(IAuthenticationManager.class);
			UserModel user = authManager.authenticate(httpRequest);
			if (user == null) {
				return false;
			}
			return user.canAdmin();
		}
		return true;
	}
}