package com.tsi.workflow.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {

    public HibernateAwareObjectMapper() {

	Hibernate4Module hm = new Hibernate4Module();
	// hm.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING)
	registerModule(hm);
	// SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	// filterProvider = filterProvider.addFilter("ConsumerFilter",
	// new MultiFilter(new HateoasBeanPropertyFilter(), new
	// DynamicPropertyFilter()));
	// filterProvider = filterProvider.addFilter("EntitlementFilter",
	// new MultiFilter(new HateoasBeanPropertyFilter(), new
	// DynamicPropertyFilter()));
	// filterProvider = filterProvider.addFilter("OwnerFilter",
	// new MultiFilter(new HateoasBeanPropertyFilter(), new
	// DynamicPropertyFilter()));
	// filterProvider = filterProvider.addFilter("GuestFilter",
	// new MultiFilter(new HateoasBeanPropertyFilter(), new
	// DynamicPropertyFilter()));
	// filterProvider.setDefaultFilter(new DynamicPropertyFilter());
	// filterProvider.setFailOnUnknownId(false);
	// this.setFilters(filterProvider);
    }

    @Override
    public ObjectWriter writer(FilterProvider filterProvider) {
	return super.writer(filterProvider); // To change body of generated methods, choose Tools | Templates.
    }

}
