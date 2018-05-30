package ch.rasc.extclassgenerator.association;

import javax.lang.model.element.Element;

public class ManyToOneAssociation extends AbstractAssociation {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public ManyToOneAssociation(Element element) {
		this(getModelName(element));
	}

	public ManyToOneAssociation(String model) {
		super("manyToOne", model);
	}
	public ManyToOneAssociation() {
		super("manyToOne");
	}
}
