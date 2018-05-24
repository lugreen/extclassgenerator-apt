/**
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bean;

import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelAssociation;
import ch.rasc.extclassgenerator.ModelAssociationType;

@Model(value = "App.TwoHasOneClass")
public class TwoHasOneClass {
	@ModelAssociation(value = ModelAssociationType.HAS_ONE, model = AssociatedClass.class)
	private AssociatedClass myFirstAssociation;

	@ModelAssociation(value = ModelAssociationType.HAS_ONE, model = AssociatedClass.class)
	private AssociatedClass mySecondAssociation;

	public AssociatedClass getMyFirstAssociation() {
		return this.myFirstAssociation;
	}

	public void setMyFirstAssociation(AssociatedClass myFirstAssociation) {
		this.myFirstAssociation = myFirstAssociation;
	}

	public AssociatedClass getMySecondAssociation() {
		return this.mySecondAssociation;
	}

	public void setMySecondAssociation(AssociatedClass mySecondAssociation) {
		this.mySecondAssociation = mySecondAssociation;
	}

}
