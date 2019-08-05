/*
 * Copyright © 2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package io.guise.mummy;

import static java.util.Objects.*;

import javax.annotation.*;

import io.confound.config.Configuration;

/**
 * Default implementation of a Guise project.
 * @author Garret Wilson
 */
public class DefaultGuiseProject implements GuiseProject {

	private final Configuration configuration;

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Project constructor.
	 * @param configuration The project configuration.
	 */
	public DefaultGuiseProject(@Nonnull final Configuration configuration) {
		this.configuration = requireNonNull(configuration);
	}

}
