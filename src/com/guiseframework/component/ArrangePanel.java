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

package com.guiseframework.component;

import com.guiseframework.component.layout.Flow;
import com.guiseframework.component.layout.FlowLayout;
import com.guiseframework.component.layout.Layout;

/**A panel that semantically demarcates an area that arranges other components.
This panel is usually styled to arrange the spacing between child components.
This is stronger than a {@link LayoutPanel}, which provides no arranging styles to the children. 
@author Garret Wilson
@see ArrangeContainer
*/
public class ArrangePanel extends LayoutPanel implements ArrangeContainer
{
	
	/**Default constructor with a default vertical flow layout.*/
	public ArrangePanel()
	{
		this(new FlowLayout(Flow.PAGE));	//default to flowing vertically
	}

	/**Layout constructor.
	@param layout The layout definition for the container.
	@exception NullPointerException if the given layout is <code>null</code>.
	*/
	public ArrangePanel(final Layout<?> layout)
	{
		super(layout);	//construct the parent class
	}
}
