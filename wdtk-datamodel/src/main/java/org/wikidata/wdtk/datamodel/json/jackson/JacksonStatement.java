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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.wikidata.wdtk.datamodel.helpers.Equality;
import org.wikidata.wdtk.datamodel.helpers.Hash;
import org.wikidata.wdtk.datamodel.helpers.ToString;
import org.wikidata.wdtk.datamodel.interfaces.Claim;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;
import org.wikidata.wdtk.datamodel.interfaces.Value;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Jackson implementation of {@link Statement}. In JSON, the corresponding
 * structures are referred to as "claim".
 * <p>
 * Like all Jackson objects, it is not technically immutable, but it is strongly
 * recommended to treat it as such in all contexts: the setters are for Jackson;
 * never call them in your code.
 *
 * @author Fredo Erxleben
 *
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JacksonStatement implements Statement {

	/**
	 * Id of this statement.
	 *
	 * @see Statement#getStatementId()
	 */
	private String id;

	/**
	 * The subject entity that this statement refers to. This is needed since it
	 * is not part of the JSON serialization of statements, but is needed in
	 * WDTK as part of {@link Claim}. Thus, it is necessary to set this
	 * information after each deserialization using
	 * {@link JacksonStatement#setSubject(EntityIdValue)}
	 * .
	 */
	@JsonIgnore
	EntityIdValue subject = null;

	/**
	 * Rank of this statement.
	 */
	@JsonSerialize(using = StatementRankSerializer.class)
	@JsonDeserialize(using = StatementRankDeserializer.class)
	private StatementRank rank;

	private List<JacksonReference> references = new ArrayList<>();

	/**
	 * The main snak of this statement.
	 */
	private JacksonSnak mainsnak;

	/**
	 * A map from property id strings to snaks that encodes the qualifiers.
	 */
	private Map<String, List<JacksonSnak>> qualifiers = new HashMap<>();
	/**
	 * List of property string ids that encodes the desired order of qualifiers,
	 * which is not specified by the map.
	 */
	private List<String> propertyOrder = Collections.emptyList();

	/**
	 * Constructor. Creates an empty object that can be populated during JSON
	 * deserialization. Should only be used by Jackson for this very purpose.
	 */
	@JsonCreator
	public JacksonStatement(
			@JsonProperty("id") String id,
			@JsonProperty("rank") StatementRank rank,
			@JsonProperty("mainsnak") JacksonSnak mainsnak,
			@JsonProperty("qualifiers") Map<String, List<JacksonSnak>> qualifiers,
			@JsonProperty("references") List<JacksonReference> references,
			@JacksonInject("subject") EntityIdValue subject) {
		this.id = id;
		Validate.notNull(rank, "No rank provided to create a statement.");
		this.rank = rank;
		Validate.notNull(mainsnak, "No main snak provided to create a statement.");
		this.mainsnak = mainsnak;
		if (qualifiers != null) {
			this.qualifiers = qualifiers;
		} else {
			this.qualifiers = Collections.<String, List<JacksonSnak>>emptyMap();
		}
		if (references != null) {
			this.references = references;
		} else {
			this.references = Collections.<JacksonReference>emptyList();
		}
		Validate.notNull(subject, "No subject provided to create a statement.");
		this.subject = subject;
	}

	/**
	 * TODO review the utility of this constructor.
	 *
	 * @param id
	 * @param mainsnak
	 */
	public JacksonStatement(String id, JacksonSnak mainsnak) {
		this.id = id;
		this.rank = StatementRank.NORMAL;
		this.mainsnak = mainsnak;
	}

	/**
	 * Returns the value for the "type" field used in JSON. Only for use by
	 * Jackson during deserialization.
	 *
	 * @return "statement"
	 */
	@JsonProperty("type")
	public String getJsonType() {
		return "statement";
	}

	/**
	 * Returns the subject that this statement refers to. This method is only
	 * used by {@link ClaimFromJson} to retrieve data about the subject of this
	 * statement. To access this data from elsewhere, use {@link #getClaim()}.
	 *
	 * @see Claim#getSubject()
	 * @return the subject of this statement
	 */
	@JsonIgnore
	EntityIdValue getSubject() {
		if (this.subject == null) {
			throw new RuntimeException(
					"Cannot retrieve subject for insufficiently initialised statement/claim.");
		} else {
			return this.subject;
		}
	}

	/**
	 * Sets the subject entity of this statement. This information is needed in
	 * WDTK but is not part of the JSON serialization. The subject is also used
	 * to obtain the IRI of the site this statement belongs to. This method
	 * should only be used during deserialization.
	 *
	 * @param subject
	 *            new value
	 */
	@JsonIgnore
	void setSubject(EntityIdValue subject) {
		this.subject = subject;
	}

	@JsonIgnore
	@Override
	public Claim getClaim() {
		return new ClaimFromJson(this);
	}

	@Override
	public StatementRank getRank() {
		return this.rank;
	}

	@Override
	public List<? extends Reference> getReferences() {
		return this.references;
	}

	@JsonProperty("id")
	@Override
	public String getStatementId() {
		return this.id;
	}

	/**
	 * Returns the main snak of the claim of this statement. Only for use by
	 * Jackson during serialization. To access this data, use
	 * {@link #getClaim()}.
	 *
	 * @return main snak
	 */
	public JacksonSnak getMainsnak() {
		return this.mainsnak;
	}

	/**
	 * Returns the qualifiers of the claim of this statement. Only for use by
	 * Jackson during serialization. To access this data, use
	 * {@link #getClaim()}.
	 *
	 * @return qualifiers
	 */
	public Map<String, List<JacksonSnak>> getQualifiers() {
		return this.qualifiers;
	}

	/**
	 * Returns the list of property ids used to order qualifiers as found in
	 * JSON. Only for use by Jackson during serialization.
	 *
	 * @return the list of property ids
	 */
	@JsonProperty("qualifiers-order")
	public List<String> getPropertyOrder() {
		return this.propertyOrder;
	}

	@Override
	@JsonIgnore
	public Value getValue() {
		return this.mainsnak.getValue();
	}

	@Override
	public int hashCode() {
		return Hash.hashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return Equality.equalsStatement(this, obj);
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
