package org.wikidata.wdtk.datamodel.json.jackson.documents;

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

import org.wikidata.wdtk.datamodel.helpers.Equality;
import org.wikidata.wdtk.datamodel.helpers.Hash;
import org.wikidata.wdtk.datamodel.helpers.ToString;
import org.wikidata.wdtk.datamodel.interfaces.DatatypeIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.json.jackson.documents.ids.DatatypeIdImpl;
import org.wikidata.wdtk.datamodel.json.jackson.documents.ids.PropertyIdImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertyDocumentImpl extends EntityDocumentImpl implements
		PropertyDocument {

	private String datatype;

	public PropertyDocumentImpl() {
		this.id = new PropertyIdImpl();
	}

	/**
	 * A constructor for generating PropertyDocumentImpl-objects from other
	 * implementations that satisfy the ItemDocument-interface. This can be used
	 * for converting other implementations into this one for later export.
	 * 
	 * @param source
	 *            is the implementation to be used as a base.
	 */
	public PropertyDocumentImpl(PropertyDocument source) {
		super(source);

		// set id
		this.id = new PropertyIdImpl(source.getPropertyId().getId());

		// set data type
		switch (source.getDatatype().getIri()) {
		case DatatypeIdValue.DT_ITEM:
			this.datatype = DatatypeIdImpl.jsonTypeItem;
			break;
		case DatatypeIdValue.DT_GLOBE_COORDINATES:
			this.datatype = DatatypeIdImpl.jsonTypeGlobe;
			break;
		case DatatypeIdValue.DT_URL:
			this.datatype = DatatypeIdImpl.jsonTypeUrl;
			break;
		case DatatypeIdValue.DT_COMMONS_MEDIA:
			this.datatype = DatatypeIdImpl.jsonTypeCommonsMedia;
			break;
		case DatatypeIdValue.DT_TIME:
			this.datatype = DatatypeIdImpl.jsonTypeTime;
			break;
		case DatatypeIdValue.DT_QUANTITY:
			this.datatype = DatatypeIdImpl.jsonTypeQuantity;
			break;
		case DatatypeIdValue.DT_STRING:
			this.datatype = DatatypeIdImpl.jsonTypeString;
			break;
		default:
			this.datatype = null;
		}
	}
	
	@JsonProperty("datatype") // correct getter name already taken by interface
	public String getDataType(){
		return this.datatype;
	}
	
	@JsonProperty("datatype") // should match getter name
	public void setDataType(String datatype){
		this.datatype = datatype;
	}

	public void setId(String id){
		this.id = new PropertyIdImpl(id);
	}
	
	@JsonIgnore
	// only here to satisfy the interface
	@Override
	public PropertyIdValue getPropertyId() {
		return (PropertyIdImpl) this.id;
	}

	@JsonIgnore
	@Override
	public DatatypeIdValue getDatatype() {
		return new DatatypeIdImpl(this.datatype);
	}

	@Override
	public String getType() {
		return typeProperty;
	}
	
	@Override
	public int hashCode() {
		return Hash.hashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return Equality.equalsPropertyDocument(this, obj);
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}