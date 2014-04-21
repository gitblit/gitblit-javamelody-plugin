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

import java.util.List;

import javax.servlet.FilterConfig;

import net.bull.javamelody.Parameter;
import ro.fortsoft.pf4j.PluginWrapper;
import ro.fortsoft.pf4j.Version;

import com.gitblit.extensions.GitblitPlugin;
import com.gitblit.manager.IRuntimeManager;
import com.gitblit.servlet.GitblitContext;
import com.gitblit.utils.StringUtils;

/**
 * The JavaMelody plugin maintains runtime state and allows for configuring the
 * internals of JavaMelody by exposing the filter config parameters as Gitblit
 * settings.
 *
 * @author James Moger
 *
 */
public class Plugin extends GitblitPlugin {

	private static Plugin instance;

	public static Plugin instance() {
		return instance;
	}

	/* the setting namespace for gitblit.properties */
	private final String namespace;

	/* a filter config wrapped by IRuntimeManager for Gitblit-based config */
	private FilterConfig filterConfig;

	public Plugin(PluginWrapper wrapper) {
		super(wrapper);
		instance = this;
		namespace = wrapper.getPluginId();
	}

	@Override
	public void start() {
		log.debug("{} STARTED.", getWrapper().getPluginId());
	}

	@Override
	public void stop() {
		log.debug("{} STOPPED.", getWrapper().getPluginId());
	}

	@Override
	public void onInstall() {
		log.debug("{} INSTALLED.", getWrapper().getPluginId());

		// install default JavaMelody settings
		IRuntimeManager runtime = GitblitContext.getManager(IRuntimeManager.class);
		List<String> keys = runtime.getSettings().getAllKeys(namespace);
		if (keys.isEmpty()) {
			runtime.getSettings().overrideSetting(asKey(Parameter.MONITORING_PATH), "/monitoring");
			runtime.getSettings().saveSettings();
		}
	}

	protected String asKey(Parameter parameter) {
		return namespace + "." + parameter.getCode();
	}

	@Override
	public void onUpgrade(Version oldVersion) {
		log.debug("{} UPGRADED from {}.", getWrapper().getPluginId(), oldVersion);
	}

	@Override
	public void onUninstall() {
		log.debug("{} UNINSTALLED.", getWrapper().getPluginId());

		// remove JavaMelody settings
		IRuntimeManager runtime = GitblitContext.getManager(IRuntimeManager.class);
		List<String> keys = runtime.getSettings().getAllKeys(namespace);
		if (!keys.isEmpty()) {
			runtime.getSettings().removeSetting(asKey(Parameter.MONITORING_PATH));
			runtime.getSettings().saveSettings();
		}
	}

	public void setConfig(FilterConfig config) {
		filterConfig = config;
	}

	public String getMonitoringPath() {
		String path = filterConfig.getInitParameter(Parameter.MONITORING_PATH.getCode());
		if (StringUtils.isEmpty(path)) {
			path = "/monitoring";
		}
		return path;
	}
}

