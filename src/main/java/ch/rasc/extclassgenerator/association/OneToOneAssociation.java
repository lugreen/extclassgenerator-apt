package ch.rasc.extclassgenerator.association;

import javax.lang.model.element.Element;

public class OneToOneAssociation extends AbstractAssociation {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OneToOneAssociation(Element element) {
		this(getModelName(element));
	}

	public OneToOneAssociation(String model) {
		super("oneToOne", model);
	}

	public OneToOneAssociation() {
		super("oneToOne");
	}
}
