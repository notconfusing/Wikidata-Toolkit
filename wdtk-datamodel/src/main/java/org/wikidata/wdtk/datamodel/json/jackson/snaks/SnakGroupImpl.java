package org.wikidata.wdtk.datamodel.json.jackson.snaks;

/*
 * #%L
 * Wikidata Toolkit Data Model
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;
import org.wikidata.wdtk.datamodel.json.jackson.documents.ids.PropertyIdImpl;

/**
 * This class is not actually used in the JSON model, but needed to satisfy the
 * WDTK-datamodel interface. Since this is used only for conversion purposes
 * between different implementations of the WDTK-interface the class does not
 * provide setters apart from the constructor.
 * 
 * @author Fredo Erxleben
 *
 */
public class SnakGroupImpl implements SnakGroup {

	private PropertyIdImpl property;
	private List<SnakImpl> snaks;

	public SnakGroupImpl(PropertyIdImpl property, List<SnakImpl> snaks){
		this.property = property;
		this.snaks = snaks;
	}
	
	@Override
	public List<Snak> getSnaks() {
		
		// because of the typing provided by the interface one has to
		// re-create the list anew, simple casting is not possible
		List<Snak> returnList = new ArrayList<>(this.snaks.size());
		for(SnakImpl snak : this.snaks){
			returnList.add(snak);
		}
		return returnList;
	}

	@Override
	public PropertyIdValue getProperty() {
		return this.property;
	}

	@Override
	public Iterator<Snak> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}