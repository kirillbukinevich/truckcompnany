package com.truckcompany.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages = "com.truckcompany.repository.search", multicoreSupport = true, schemaCreationSupport = true)
public class SolrConfiguration implements EnvironmentAware {


    private final Logger log = LoggerFactory.getLogger(SolrConfiguration.class);

    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_SOLR);
    }

    private static final String ENV_SOLR = "spring.data.solr.";
    private static final String SOLR_HOST = "host";


    @Bean
    public SolrClient solrClient() {
        String solrHost = this.propertyResolver.getRequiredProperty(SOLR_HOST);
        log.debug("Configuring SOLR base url: " + solrHost);
        return new HttpSolrClient(solrHost);
    }


/*
   @Bean
    public SolrTemplate solrTemplate(SolrClient server) throws Exception {
        return new SolrTemplate(new HttpSolrServerFactory(server));
    }*/
}

