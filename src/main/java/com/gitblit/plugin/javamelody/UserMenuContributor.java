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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ro.fortsoft.pf4j.Extension;

import com.gitblit.extensions.UserMenuExtension;
import com.gitblit.models.Menu.ExternalLinkMenuItem;
import com.gitblit.models.Menu.MenuItem;
import com.gitblit.models.UserModel;

/**
 * Adds an admin menu item for JavaMelody.
 *
 * @author James Moger
 *
 */
@Extension
public class UserMenuContributor extends UserMenuExtension {

	@Override
	public List<MenuItem> getMenuItems(UserModel user) {
		if (user.canAdmin()) {
			String path = Plugin.instance().getMonitoringPath();
			return Arrays.asList((MenuItem) new ExternalLinkMenuItem("JavaMelody", path));
		}
		return Collections.emptyList();
	}

}
