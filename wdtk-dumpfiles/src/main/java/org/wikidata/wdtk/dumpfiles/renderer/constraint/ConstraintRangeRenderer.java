package org.wikidata.wdtk.dumpfiles.renderer.constraint;

/*
 * #%L
 * Wikidata Toolkit Dump File Handling
 * %%
 * Copyright (C) 2014 Wikidata Toolkit Developers
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.dumpfiles.constraint.Constraint;
import org.wikidata.wdtk.dumpfiles.constraint.ConstraintRange;
import org.wikidata.wdtk.dumpfiles.renderer.format.RendererFormat;
import org.wikidata.wdtk.dumpfiles.renderer.format.StringResource;

/**
 * 
 * @author Julian Mendez
 * 
 */
class ConstraintRangeRenderer implements ConstraintRenderer {

	final RendererFormat f;

	public ConstraintRangeRenderer(RendererFormat rendererFormat) {
		this.f = rendererFormat;
	}

	@Override
	public void renderConstraint(Constraint c) {
		if (c instanceof ConstraintRange) {
			render((ConstraintRange) c);
		}
	}

	public void render(ConstraintRange c) {
		if (c.isQuantity()) {
			renderQuantity(c.getConstrainedProperty(),
					new StringResource(c.getMin()),
					new StringResource(c.getMax()));
		}
		if (c.isTime()) {
			renderTime(c.getConstrainedProperty(),
					new StringResource(c.getMin()),
					new StringResource(c.getMax()));
		}
	}

	public void renderQuantity(PropertyIdValue p, Resource min, Resource max) {
		render(p, new StringResource(":value"), min, max, this.f.xsdDecimal()); // FIXME
																				// fix
																				// parameter
	}

	public void renderTime(PropertyIdValue p, Resource min, Resource max) {
		render(p, new StringResource(":time"), min, max, this.f.xsdDateTime()); // FIXME
																				// fix
																				// parameter
	}

	public void render(PropertyIdValue p, Resource param, Resource min,
			Resource max, Resource type) {
		if ((p == null) || (param == null)) {
			return;
		}
		URI rp = this.f.aRp(p);
		this.f.addInverseFunctionalObjectProperty(this.f.a_s(p));
		this.f.addDatatypeDefinition(
				rp,
				this.f.getDataIntersectionOf(
						this.f.getDatatypeRestriction(type,
								this.f.xsdMinInclusive(),
								this.f.getLiteral(min, type)),
						this.f.getDatatypeRestriction(type,
								this.f.xsdMaxInclusive(),
								this.f.getLiteral(max, type))));
		this.f.addObjectPropertyRange(this.f.a_v(p),
				this.f.getDataSomeValuesFrom(param, rp));
	}

}
