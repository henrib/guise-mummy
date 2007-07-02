package com.guiseframework.component;

import com.guiseframework.component.layout.*;

/**Panel to display information about an object such as the application.
@author Garret Wilson
*/
public class AboutPanel extends AbstractPanel
{

	/**The name label.*/
	private final Label nameLabel;

	/**The version label.*/
	private final Label versionLabel;

	/**The copyright label.*/
	private final Label copyrightLabel;

	/**Default constructor with a default vertical flow layout.*/
	public AboutPanel()
	{
		super(new FlowLayout(Flow.PAGE));	//construct the parent class

		nameLabel=new Label();
		add(nameLabel);

		versionLabel=new Label();
		add(versionLabel);

		copyrightLabel=new Label();
		add(copyrightLabel);
	}

	/**@return The name label, or <code>null</code> if there is no name label.*/
	public String getNameLabel()
	{
		return nameLabel.getLabel();
	}

	/**Sets the name label shown in the panel.
	@param name The name to show.
	*/
	public void setNameLabel(final String name)
	{
		nameLabel.setLabel(name);
	}

	/**@return The version label, or <code>null</code> if there is no version label.*/
	public String getVersionLabel()
	{
		return versionLabel.getLabel();
	}

	/**Sets the version label shown in the panel.
	@param version The version to show.
	*/
	public void setVersionLabel(final String version)
	{
		versionLabel.setLabel(version);
	}

	/**@return The copyright label, or <code>null</code> if there is no copyright label.*/
	public String getCopyrightLabel()
	{
		return copyrightLabel.getLabel();
	}

	/**Sets the copyright label shown in the panel.
	@param copyright The copyright to show.
	*/
	public void setCopyrightLabel(final String copyright)
	{
		copyrightLabel.setLabel(copyright);
	}

}