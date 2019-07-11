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

package io.guise.catalina.webresources;

import static com.globalmentor.net.URIs.*;
import static java.util.Objects.*;

import java.io.File;
import java.nio.file.Path;

import javax.annotation.*;

import org.apache.catalina.*;
import org.apache.catalina.webresources.*;

/**
 * Guise Tomcat site root.
 * @author Garret Wilson
 */
public class SiteRoot extends StandardRoot {

	private final Path descriptionBaseDir;

	/**
	 * Creates the root with a record of the directory used for descriptions of site resources.
	 * @param descriptionBaseDir The root directory of the description metadata tree, which may or may not be the same as the site doc base.
	 */
	public SiteRoot(@Nonnull final Path descriptionBaseDir) {
		this.descriptionBaseDir = requireNonNull(descriptionBaseDir);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns a specialized {@link SiteDirResourceSet} to serve as a factory for creating specialized Guise-aware file resources.
	 */
	@Override
	protected WebResourceSet createMainResourceSet() {
		final Context context = getContext();
		final String docBase = context.getDocBase();
		if(docBase != null) {
			File baseFile = new File(docBase);
			if(!baseFile.isAbsolute()) {
				baseFile = new File(((Host)context.getParent()).getAppBaseFile(), baseFile.getPath());
			}
			if(baseFile.isDirectory()) {
				return new SiteDirResourceSet(this, ROOT_PATH, baseFile.getAbsolutePath(), ROOT_PATH, descriptionBaseDir);
			}
		}
		return super.createMainResourceSet();
	}

}
