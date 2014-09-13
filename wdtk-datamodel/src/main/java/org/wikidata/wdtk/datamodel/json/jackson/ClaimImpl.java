package org.wikidata.wdtk.datamodel.json.jackson;

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

import java.util.Iterator;
import java.util.List;

import org.wikidata.wdtk.datamodel.helpers.Equality;
import org.wikidata.wdtk.datamodel.helpers.Hash;
import org.wikidata.wdtk.datamodel.helpers.ToString;
import org.wikidata.wdtk.datamodel.interfaces.Claim;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;

/**
 * This class only exists to satisfy the interface of the data model.
 * @author Fredo Erxleben
 *
 */
public class ClaimImpl implements Claim {

	private StatementImpl statement;
	private EntityIdValue subject;
	
	public ClaimImpl(StatementImpl statement, EntityIdValue subject){
		this.statement = statement;
		this.subject = subject;
	}
	
	@Override
	public EntityIdValue getSubject() {
		return this.subject;
	}

	@Override
	public Snak getMainSnak() {
		return this.statement.getMainsnak();
	}

	@Override
	public List<SnakGroup> getQualifiers() {
		return Helper.buildSnakGroups(this.statement.getQualifiers());
	}

	@Override
	public Iterator<Snak> getAllQualifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		return Hash.hashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return Equality.equalsClaim(this, obj);
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}