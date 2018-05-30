package ch.rasc.extclassgenerator.association;

import javax.lang.model.element.Element;

public class OneToManyAssociation extends AbstractAssociation {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public OneToManyAssociation(Element element) {
		this(getModelName(element));
	}

	public OneToManyAssociation(String model) {
		super("oneToMany", model);
	}
	public OneToManyAssociation() {
		super("oneToMany");
	}
}
