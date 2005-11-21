package com.javaguise.component;

import com.javaguise.model.ActionValueModel;
import com.javaguise.model.DefaultActionValueModel;
import com.javaguise.session.GuiseSession;

/**Button that stores a boolean value in its model representing the selected state.
A validator requiring a non-<code>null</code> value is automatically installed.
@author Garret Wilson
*/
public class ToggleButton extends AbstractToggleButtonControl<ToggleButton>
{

	/**Session constructor with a default data model.
	@param session The Guise session that owns this component.
	@exception NullPointerException if the given session is <code>null</code>.
	*/
	public ToggleButton(final GuiseSession session)
	{
		this(session, (String)null);	//construct the component, indicating that a default ID should be used
	}

	/**Session and ID constructor with a default data model.
	@param session The Guise session that owns this component.
	@param id The component identifier, or <code>null</code> if a default component identifier should be generated.
	@exception NullPointerException if the given session is <code>null</code>.
	@exception IllegalArgumentException if the given identifier is not a valid component identifier.
	*/
	public ToggleButton(final GuiseSession session, final String id)
	{
		this(session, id, new DefaultActionValueModel<Boolean>(session, Boolean.class, Boolean.FALSE));	//construct the class with a default model
	}

	/**Session and model constructor.
	@param session The Guise session that owns this component.
	@param model The component data model.
	@exception NullPointerException if the given session and/or model is <code>null</code>.
	*/
	public ToggleButton(final GuiseSession session, final ActionValueModel<Boolean> model)
	{
		this(session, null, model);	//construct the component, indicating that a default ID should be used
	}

	/**Session, ID, and model constructor.
	@param session The Guise session that owns this component.
	@param id The component identifier, or <code>null</code> if a default component identifier should be generated.
	@param model The component data model.
	@exception NullPointerException if the given session and/or model is <code>null</code>.
	@exception IllegalArgumentException if the given identifier is not a valid component identifier.
	*/
	public ToggleButton(final GuiseSession session, final String id, final ActionValueModel<Boolean> model)
	{
		super(session, id, model);	//construct the parent class
	}

}
