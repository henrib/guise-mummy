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

package io.guise.mummy.mummify;

import static com.globalmentor.io.Paths.*;
import static java.nio.file.Files.*;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import javax.annotation.*;

import io.guise.mummy.MummyContext;
import io.urf.model.*;
import io.urf.turf.TurfParser;

/**
 * Abstract mummifier for generating artifacts based upon a single source file or directory.
 * @author Garret Wilson
 */
public abstract class AbstractSourcePathMummifier implements SourcePathMummifier {

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation merely changes the base between source and target directory trees.
	 * @see MummyContext#getSiteSourceDirectory()
	 * @see MummyContext#getSiteTargetDirectory()
	 */
	@Override
	public Path getArtifactTargetPath(@Nonnull MummyContext context, @Nonnull final Path sourcePath) {
		return changeBase(sourcePath, context.getSiteSourceDirectory(), context.getSiteTargetDirectory());
	}

	/**
	 * Loads the generated target description if any of a source file.
	 * @param context The context of static site generation.
	 * @param sourcePath The path in the site source directory.
	 * @throws IllegalArgumentException if the given source file is not in the site source tree.
	 * @return The generated target description, if present, of the resource being mummified.
	 * @throws IOException if there is an I/O error retrieving the description, including if the metadata is invalid.
	 * @see #getArtifactDescriptionFile(MummyContext, Path)
	 */
	protected Optional<UrfResourceDescription> loadTargetDescription(@Nonnull MummyContext context, @Nonnull final Path sourcePath) throws IOException {
		final Path descriptionFile = getArtifactDescriptionFile(context, sourcePath);
		if(!isRegularFile(descriptionFile)) {
			return Optional.empty();
		}
		try (final InputStream inputStream = new BufferedInputStream(newInputStream(descriptionFile))) {
			return new TurfParser<List<Object>>(new SimpleGraphUrfProcessor()).parseDocument(inputStream).stream().filter(UrfResourceDescription.class::isInstance)
					.map(UrfResourceDescription.class::cast).findFirst();
		}
	}

}