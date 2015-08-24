/*
 * Copyright © 2005-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.guiseframework.platform.web;

import java.util.Map;

import static com.globalmentor.w3c.spec.CSS.*;
import static com.globalmentor.w3c.spec.HTML.*;

import com.guiseframework.component.*;

/**
 * Strategy for rendering a scroll control as an XHTML <code>&lt;div&gt;</code> element.
 * @param <GC> The type of Guise context.
 * @param <C> The type of component being depicted.
 * @author Garret Wilson
 */
public class WebScrollControlDepictor<GC extends WebDepictContext, C extends ScrollControl> extends AbstractWebComponentDepictor<C> {

	/** Default constructor using the XHTML <code>&lt;div&gt;</code> element. */
	public WebScrollControlDepictor() {
		super(XHTML_NAMESPACE_URI, ELEMENT_DIV); //represent <xhtml:div>
		//TODO check the notification property ignoring and other similar statements in the layout component depictor
		getIgnoredProperties().add(LayoutComponent.NOTIFICATION_PROPERTY); //ignore Panel.notification, because we don't want to mark the component invalid when it registers a notification as this is used to pass a notification up to an enclosing class
	}

	/**
	 * Retrieves the styles for the outer element of the component. This version combines the body styles with the outer styles. This version adds CSS scroll
	 * properties.
	 * @return The styles for the outer element of the component, mapped to CSS property names.
	 * @see AbstractWebComponentDepictor#getBodyStyles()
	 */
	protected Map<String, Object> getOuterStyles() {
		final Map<String, Object> outerStyles = super.getOuterStyles(); //get the default outer styles
		outerStyles.putAll(getBodyStyles()); //add the styles for the body
		outerStyles.put(CSS_PROP_OVERFLOW, CSS_OVERFLOW_AUTO); //set overflow: auto TODO allow this to be customized by the component
		return outerStyles; //return the combined styles		
	}
}
