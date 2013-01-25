package org.lesornithorynquesasthmatiques.solr;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.util.ClientUtils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Helper class to leverage the creation of {@link SolrQuery} objects.
 * @see "http://lucene.apache.org/core/old_versioned_docs/versions/2_9_1/queryparsersyntax.html"
 * 
 * @author Alexandre Dutra
 *
 */
public class SolrQueryBuilder {

	private static final String REQUIRED = "+";

	private static final String GET_ALL = "*:*";
	
	private static final MapJoiner JOINER = Joiner.on(" AND ").withKeyValueSeparator(":");

	private static final Pattern WHITESPACE = Pattern.compile("\\s");

	private static final Splitter SPLITTER = Splitter.on(WHITESPACE).omitEmptyStrings().trimResults();

	private static final Function<Entry<String,String>,String> ENTRY_JOIN = new Function<Entry<String,String>,String>() {
		@Override
		public String apply(@Nullable Entry<String,String> input) {
			return input.getKey() + ":" + input.getValue();
		}
	};
	
	private Multimap<String, String> queryFields = LinkedHashMultimap.create();

	private Multimap<String, String> filterQueryFields = LinkedHashMultimap.create();
	
	private Map<String,ORDER> sortFields = new LinkedHashMap<>();

	private Integer start;

	private Integer pageSize;

	private Integer pageNumber;
	
	public SolrQueryBuilder(){
	}

	/**
	 * Builds the {@link SolrQuery} object.
	 * @return
	 */
	public SolrQuery build(){
		String query = JOINER.join(queryFields.entries());
		if(query.isEmpty()) query = GET_ALL;
		SolrQuery sq = new SolrQuery(query);
		sq.addFilterQuery(Iterables.toArray(Iterables.transform(filterQueryFields.entries(), ENTRY_JOIN), String.class));
		for (Entry<String,ORDER> entry : sortFields.entrySet()) {
			sq.addSortField(entry.getKey(), entry.getValue());
		}
		if(start != null){
			sq.setStart(start);
		} else if(pageNumber != null && pageSize != null) {
			sq.setStart((pageNumber - 1) * pageSize);
		}
		if(pageSize != null){
			sq.setRows(pageSize);
		}
		return sq;
	}

	/**
	 * Checks whether the given search is not empty, and if so, adds it as a "raw" query field. 
	 * The given search is added as is.
	 * CAUTION: DO NOT use this method to add a user-supplied input to the query.
	 * 
	 * @param field
	 * @param search
	 * @return
	 */
	public SolrQueryBuilder withRawQueryField(String field, String search) {
		if(notEmpty(search)) queryFields.put(field, search);
		return this;
	}

	/**
	 * Checks whether the given search is not empty, and if so, adds it as a "raw", required query field. 
	 * The given search is added as is.
	 * CAUTION: DO NOT use this method to add a user-supplied input to the query.
	 * 
	 * @param field
	 * @param search
	 * @return
	 */
	public SolrQueryBuilder withRawRequiredQueryField(String field, String search) {
		return withRawQueryField(REQUIRED + field, search);
	}

	/**
	 * Checks whether the given search is not empty, and if so, adds it as a query field.
	 * The given search is handled as follows:
	 * <ol>
	 * <li>The search is split into tokens (whitespace split);</li>
	 * <li>Each token is escaped (see <code>{@link ClientUtils#escapeQueryChars(String)}</code>);</li>
	 * <li>Each escaped token is added as a query field.</li>
	 * </ol>
	 * This method is suitable to use with user-supplied input.
	 * @param field
	 * @param search
	 * @return
	 */
	public SolrQueryBuilder withQueryField(String field, String search) {
		if(notEmpty(search)) tokenize(field, search, queryFields);
		return this;
	}

	/**
	 * Checks whether the given search is not empty, and if so, adds it as a required query field.
	 * The given search is handled as follows:
	 * <ol>
	 * <li>The search is split into tokens (whitespace split);</li>
	 * <li>Each token is escaped (see <code>{@link ClientUtils#escapeQueryChars(String)}</code>);</li>
	 * <li>Each escaped token is added as a required query field.</li>
	 * </ol>
	 * This method is suitable to use with user-supplied input.
	 * @param field
	 * @param search
	 * @return
	 */
	public SolrQueryBuilder withRequiredQueryField(String field, String search) {
		return withQueryField(REQUIRED + field, search);
	}

	/**
	 * Checks whether the given search is not empty, and if so, adds it as a filter query field.
	 * The given search is handled as follows:
	 * <ol>
	 * <li>The search is split into tokens (whitespace split);</li>
	 * <li>Each token is escaped (see <code>{@link ClientUtils#escapeQueryChars(String)}</code>);</li>
	 * <li>Each escaped token is added as a filter query field.</li>
	 * </ol>
	 * This method is suitable to use with user-supplied input.
	 * @param field
	 * @param search
	 * @return
	 */
	public SolrQueryBuilder withFilterQueryField(String field, String search) {
		if(notEmpty(search)) tokenize(field, search, filterQueryFields);
		return this;
	}

	/**
	 * Adds a new sort field.
	 * @param field
	 * @param order
	 * @return
	 */
	public SolrQueryBuilder withSortField(String field, ORDER order) {
		sortFields.put(field, order);
		return this;
	}

	/**
	 * Adds a new sort field in ascending order.
	 * @param field
	 * @return
	 */
	public SolrQueryBuilder withAscSortField(String field) {
		return withSortField(field, ORDER.asc);
	}

	/**
	 * Adds a new sort field in descending order.
	 * @param field
	 * @return
	 */
	public SolrQueryBuilder withDescSortField(String field) {
		return withSortField(field, ORDER.desc);
	}

	/**
	 * Adds a starting offset.
	 * @param start a zero-based offset
	 * @return
	 */
	public SolrQueryBuilder withStartOffset(int start) {
		this.start = start;
		return this;
	}

	/**
	 * Sets the page size.
	 * @param pageSize
	 * @return
	 */
	public SolrQueryBuilder withPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * Sets the page number. If this is supplied, then a page size must also be supplied.
	 * @param pageNumber a one-based page number
	 * @return
	 */
	public SolrQueryBuilder withPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	private boolean notEmpty(String search) {
		return search != null && ! search.isEmpty();
	}

	private void tokenize(String field, String search, Multimap<String, String> map) {
		Iterable<String> tokens = SPLITTER.split(search);
		for (String token : tokens) {
			map.put(field, escape(token));
		}
	}

	private String escape(String search) {
		return ClientUtils.escapeQueryChars(search.trim());
	}


}
