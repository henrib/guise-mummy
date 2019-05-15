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

import static java.nio.file.Files.newInputStream;

import java.io.*;
import java.nio.file.Path;
import java.util.function.Function;

import javax.annotation.*;

import org.w3c.dom.*;

/**
 * Mummifier for generating HTML pages.
 * @implSpec This type of mummifier only works with {@link SourceFileArtifact}s. If some other type of artifact is used, the mummification methods may throw a
 *           {@link ClassNotFoundException}.
 * @author Garret Wilson
 */
public interface PageMummifier extends Mummifier {

	/** The attribute for regenerating an element, such as a navigation list. */
	public static final String ATTRIBUTE_REGENERATE = "regenerate";

	/**
	 * {@inheritDoc}
	 * @implSpec A page mummifier will always return a {@link PageArtifact}.
	 */
	@Override
	PageArtifact plan(MummyContext context, Path sourcePath) throws IOException;

	//#load

	/**
	 * Loads the source file and returns it as a document to be further refined before being used to generate the artifact.
	 * <p>
	 * The returned document will not yet have been processed. For example, no expressions will have been evaluated and links will still reference source paths.
	 * </p>
	 * <p>
	 * The document must be in XHTML using the HTML namespace.
	 * </p>
	 * @implSpec The default implementation opens an input stream to the given file and then loads the source document by calling
	 *           {@link #loadSourceDocument(MummyContext, InputStream)}.
	 * @param context The context of static site generation.
	 * @param sourceFile The file from which to load the document.
	 * @return A document describing the source content of the artifact to generate.
	 * @throws IOException if there is an error loading and/or converting the source file contents.
	 * @throws DOMException if there is some error manipulating the XML document object model.
	 */
	public default Document loadSourceDocument(@Nonnull MummyContext context, @Nonnull Path sourceFile) throws IOException, DOMException {
		try (final InputStream inputStream = new BufferedInputStream(newInputStream(sourceFile))) {
			return loadSourceDocument(context, inputStream);
		}
	}

	/**
	 * Loads the source of some artifact and returns it as a document to be further refined before being used to generate the artifact.
	 * <p>
	 * The returned document will not yet have been processed. For example, no expressions will have been evaluated and links will still reference source paths.
	 * </p>
	 * <p>
	 * The document must be in XHTML using the HTML namespace.
	 * </p>
	 * @implSpec The default implementation opens an input stream using {@link SourceFileArtifact#openSource(MummyContext)} and then loads the source document by
	 *           calling {@link #loadSourceDocument(MummyContext, InputStream)}.
	 * @param context The context of static site generation.
	 * @param artifact The artifact for which to load the document.
	 * @return A document describing the source content of the artifact to generate.
	 * @throws IOException if there is an error loading and/or converting the source file contents.
	 * @throws DOMException if there is some error manipulating the XML document object model.
	 */
	public default Document loadSourceDocument(@Nonnull MummyContext context, @Nonnull SourceFileArtifact artifact) throws IOException, DOMException {
		try (final InputStream inputStream = new BufferedInputStream(artifact.openSource(context))) {
			return loadSourceDocument(context, inputStream);
		}
	}

	/**
	 * Loads a source document from the given input stream and returns it as a document to be further refined before being used to generate the artifact.
	 * <p>
	 * The returned document will not yet have been processed. For example, no expressions will have been evaluated and links will still reference source paths.
	 * </p>
	 * <p>
	 * The document must be in XHTML using the HTML namespace.
	 * </p>
	 * @param context The context of static site generation.
	 * @param inputStream The input stream from which to to load the document.
	 * @return A document describing the source content of the artifact to generate.
	 * @throws IOException if there is an error loading and/or converting the source file contents.
	 * @throws DOMException if there is some error manipulating the XML document object model.
	 */
	public Document loadSourceDocument(@Nonnull MummyContext context, @Nonnull InputStream inputStream) throws IOException, DOMException;

	//#relocate

	/**
	 * Relocates a document by retargeting its references relative to a new referrer path location.
	 * @param context The context of static site generation.
	 * @param sourceDocument The source document to relocate.
	 * @param originalReferrerSourcePath The absolute original path of the referrer, e.g. <code>…/foo/page.xhtml</code>.
	 * @param relocatedReferrerPath The absolute relocated path of the referrer, e.g. <code>…/bar/page.xhtml</code>.
	 * @param referentArtifactPath The function for determining the path of the determined referent artifact. This function should return either the source path
	 *          or the destination path of the artifact concordant with the site tree of the relocated referrer.
	 * @return The relocated document, which may or may not be the same document supplied as input.
	 * @throws IOException if there is an error relocating the document.
	 * @throws DOMException if there is some error manipulating the XML document object model.
	 */
	public Document relocateDocument(@Nonnull MummyContext context, @Nonnull final Document sourceDocument, @Nonnull final Path originalReferrerSourcePath,
			@Nonnull final Path relocatedReferrerPath, @Nonnull final Function<Artifact, Path> referentArtifactPath) throws IOException, DOMException;

}