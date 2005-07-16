package com.javaguise.event;

import com.javaguise.session.GuiseSession;

/**An event indicating a collection has been modified.
If a single element was replaced both an added and removed element will be provided.
If neither an added nor a removed element are provided, the event represents a general collection modification.
@param <S> The type of the event source.
@param <E> The type of elements contained in the collection.
@author Garret Wilson
*/
public class CollectionEvent<S, E> extends GuiseEvent<S>
{

	/**The element that was added to the collection, or <code>null</code> if no element was added or it is unknown whether or which elements were added.*/
	private E addedElement;

		/**@return The element that was added to the collection, or <code>null</code> if no element was added or it is unknown whether or which elements were added.*/
		public E getAddedElement() {return addedElement;}

	/**The element that was removed from the collection, or <code>null</code> if no element was removed or it is unknown whether or which elements were removed.*/
	private E removedElement;

		/**@return The element that was removed from the collection, or <code>null</code> if no element was added or it is unknown whether or which elements were removed.*/
		public E getRemovedElement() {return removedElement;}

	/**Session and source constructor for general collection modification.
	@param session The Guise session in which this event was generated.
	@param source The object on which the event initially occurred.
	@exception NullPointerException if the given session and/or source is <code>null</code>.
	*/
	public CollectionEvent(final GuiseSession<?> session, final S source)
	{
		this(session, source, null, null);	//construct the class with no known modification values
	}

	/**Session and source constructor for an added and/or removed element.
	@param session The Guise session in which this event was generated.
	@param source The object on which the event initially occurred.
	@param addedElement The element that was added to the collection, or <code>null</code> if no element was added or it is unknown whether or which elements were added.
	@param removedElement The element that was removed from the collection, or <code>null</code> if no element was removed or it is unknown whether or which elements were removed.
	@exception NullPointerException if the given session and/or source is <code>null</code>.
	*/
	public CollectionEvent(final GuiseSession<?> session, final S source, final E addedElement, final E removedElement)
	{
		super(session, source);	//construct the parent class
		this.addedElement=addedElement;
		this.removedElement=removedElement;
	}
}