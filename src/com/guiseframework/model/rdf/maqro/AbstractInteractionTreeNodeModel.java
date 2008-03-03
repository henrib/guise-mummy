package com.guiseframework.model.rdf.maqro;

import com.globalmentor.rdf.maqro.FollowupEvaluation;
import com.globalmentor.rdf.maqro.Interaction;
import com.guiseframework.model.rdf.AbstractRDFResourceTreeNodeModel;

/**Abstract functionality for a tree node model that represents an interaction.
This class by default does not include resource children or resource properties.
@param <V> The type of value contained in the tree node.
@author Garret Wilson
*/
public abstract class AbstractInteractionTreeNodeModel<V extends Interaction> extends AbstractRDFResourceTreeNodeModel<V>
{

	/**The followup evaluation which considers this interaction a followup in this context, or <code>null</code> if there is no followup evaluation subject in this context.*/
	private final FollowupEvaluation followupEvaluation;

		/**@return The followup evaluation which considers this interaction a followup in this context, or <code>null</code> if there is no followup evaluation subject in this context.*/
		public FollowupEvaluation getFollowupEvaluation() {return followupEvaluation;}
		
	/**Value class constructor with no initial value.
	@param valueClass The class indicating the type of value held in the model.
	*/
	public AbstractInteractionTreeNodeModel(final Class<V> valueClass)
	{
		this(valueClass, null);	//construct the class with no initial value
	}

	/**Initial value constructor.
	@param valueClass The class indicating the type of value held in the model.
	@param initialValue The initial value, which will not be validated.
	*/
	public AbstractInteractionTreeNodeModel(final Class<V> valueClass, final V initialValue)
	{
		this(valueClass, null, initialValue);	//construct the class with no property
	}

	/**Property and initial value constructor.
	@param valueClass The class indicating the type of value held in the model.
	@param followupEvaluation The followup evaluation which considers this interaction a followup in this context, or <code>null</code> if there is no followup evaluation subject in this context.
	@param initialValue The initial value, which will not be validated.
	*/
	public AbstractInteractionTreeNodeModel(final Class<V> valueClass, final FollowupEvaluation followupEvaluation, final V initialValue)
	{
		super(valueClass, initialValue);	//construct the parent class
		this.followupEvaluation=followupEvaluation; //save the evaluation of which this interaction is the followup
		setResourceChildrenIncluded(false);	//don't show resource children
		setResourcePropertiesIncluded(false);	//don't show resource properties
	}

}
