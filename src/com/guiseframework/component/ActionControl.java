package com.guiseframework.component;

import com.guiseframework.model.ActionModel;

/**A general control with an action model.
@author Garret Wilson
*/
public interface ActionControl<C extends ActionControl<C>> extends Control<C>
{

	/**@return The data model used by this component.*/
	public ActionModel getModel();

}