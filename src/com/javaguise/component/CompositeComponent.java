package com.javaguise.component;

/**A component that can contain other components.
A composite component may contain other components, but only a {@link Container} allows for custom addition and removal of child components.
A composite component is iterable over its child components, and can be used in short <code>for(:)</code> form. 
@author Garret Wilson
*/
public interface CompositeComponent<C extends CompositeComponent<C>> extends Component<C>, Iterable<Component<?>>
{

	/**@return Whether this component has children.*/
	public boolean hasChildren();

	/**Retrieves the child component with the given ID.
	@param id The ID of the component to return.
	@return The child component with the given ID, or <code>null</code> if there is no child component with the given ID. 
	*/
	public Component<?> getComponent(final String id);

}
