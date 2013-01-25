package org.lesornithorynquesasthmatiques.solr;

import static org.fest.assertions.api.Assertions.*;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Test;

public class SolrQueryBuilderTest {

	@Test
	public void should_return_get_all(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("*:*");
	}

	@Test
	public void should_record_all_values_for_same_field(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		sqb.withQueryField("foo", "bar");
		sqb.withQueryField("foo", "baz");
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("foo:bar AND foo:baz");
	}

	@Test
	public void should_tokenize_query_fileds(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		sqb.withQueryField("foo", "bar baz");
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("foo:bar AND foo:baz");
	}

	@Test
	public void should_not_tokenize_raw_query_fileds(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		sqb.withRawQueryField("foo", "(bar OR baz)");
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("foo:(bar OR baz)");
	}

	@Test
	public void should_not_tokenize_raw_required_query_fileds(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		sqb.withRawRequiredQueryField("foo", "(bar OR baz)");
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("+foo:(bar OR baz)");
	}

	@Test
	public void should_record_all_values_for_same_field_required(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		sqb.withRequiredQueryField("foo", "bar");
		sqb.withQueryField("foo", "baz");
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("+foo:bar AND foo:baz");
	}

	@Test
	public void should_not_record_same_value_twice_for_same_field(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		sqb.withQueryField("foo", "bar");
		sqb.withQueryField("foo", "bar");
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("foo:bar");
	}

	@Test
	public void should_tokenize_required_fields(){
		SolrQueryBuilder sqb = new SolrQueryBuilder();
		sqb.withRequiredQueryField("foo", "bar baz");
		SolrQuery query = sqb.build();
		assertThat(query.getQuery()).isEqualTo("+foo:bar AND +foo:baz");
	}

	@Test
	public void should_record_sort_fields(){
		SolrQuery query = new SolrQueryBuilder().
			withAscSortField("foo").
			withDescSortField("bar").
			build();
		assertThat(query.getSortFields()).containsSequence("foo asc", "bar desc");
	}

	@Test
	public void should_record_query_filters(){
		SolrQuery query = new SolrQueryBuilder().
			withFilterQueryField("foo", "bar").
			withFilterQueryField("foo", "baz").
			withFilterQueryField("bar", "qix").
			build();
		assertThat(query.getFilterQueries()).containsSequence("foo:bar", "foo:baz", "bar:qix");
	}

	@Test
	public void should_tokenize_filter_queries(){
		SolrQuery query = new SolrQueryBuilder().
			withFilterQueryField("foo", "bar baz").
			withFilterQueryField("bar", "qix").
			build();
		assertThat(query.getFilterQueries()).containsSequence("foo:bar", "foo:baz", "bar:qix");
	}

	@Test
	public void should_record_start(){
		SolrQuery query = new SolrQueryBuilder().build();
		assertThat(query.getStart()).isNull();
		query = new SolrQueryBuilder()
			.withStartOffset(42)
			.build();
		assertThat(query.getStart()).isEqualTo(42);
	}

	@Test
	public void should_record_page_size(){
		SolrQuery query = new SolrQueryBuilder().build();
		assertThat(query.getRows()).isNull();
		query = new SolrQueryBuilder()
			.withPageSize(42)
			.build();
		assertThat(query.getRows()).isEqualTo(42);
	}

	@Test
	public void should_record_page_number(){
		SolrQuery query = new SolrQueryBuilder().build();
		assertThat(query.getRows()).isNull();
		query = new SolrQueryBuilder()
			.withPageSize(42)
			.withPageNumber(2)
			.build();
		assertThat(query.getRows()).isEqualTo(42);
		assertThat(query.getStart()).isEqualTo(42);
	}

}
