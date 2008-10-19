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

package com.guiseframework.model;

import static com.globalmentor.java.Objects.*;
import static com.globalmentor.text.Text.*;

import com.globalmentor.net.ContentType;
import com.globalmentor.beans.*;
import com.globalmentor.event.EventListenerManager;

/**A base abstract class implementing helpful functionality for models.
@author Garret Wilson
*/
public abstract class AbstractModel extends BoundPropertyObject implements Model
{
	/**The object managing event listeners.*/
	private final EventListenerManager eventListenerManager=new EventListenerManager();

		/**@return The object managing event listeners.*/
		protected EventListenerManager getEventListenerManager() {return eventListenerManager;}

	/**Default constructor.*/
	public AbstractModel()
	{
	}

	/**Determines the plain text form of the given text, based upon its content type.
	@param text The given text.
	@param contentType The content type of the text.
	@return The plain text form of the given text, based upon the given content type.
	@exception NullPointerException if the given text and/or content type is <code>null</code>.
	@exception IllegalArgumentException if the given content type is not a text content type.
	*/
	public static String getPlainText(final String text, final ContentType contentType)	//TODO del or move
	{
		checkInstance(text, "Text cannot be null");
		checkInstance(contentType, "Content Type cannot be null.");
		if(!isText(contentType))	//if the new content type is not a text content type
		{
			throw new IllegalArgumentException("Content type "+contentType+" is not a text content type.");
		}
		return text;	//TODO fix to actually convert to plain text
	}

}
