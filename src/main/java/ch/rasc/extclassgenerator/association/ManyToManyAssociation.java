package ch.rasc.extclassgenerator.association;

import javax.lang.model.element.Element;

public class ManyToManyAssociation extends AbstractAssociation {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ManyToManyAssociation(Element element) {
		this(getModelName(element));
	}

	public ManyToManyAssociation(String model) {
		super("manyToMany", model);
	}
	public ManyToManyAssociation() {
		super("manyToMany");
	}
}
