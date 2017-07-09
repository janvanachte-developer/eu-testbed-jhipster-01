package eu.europa.ec.digit.bris.testbed.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.MetaData.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.Actor.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.Domain.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.Domain.class.getName() + ".specifications", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.Specification.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.Specification.class.getName() + ".testSuites", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestSuite.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestSuite.class.getName() + ".actors", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestSuite.class.getName() + ".testCases", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestCase.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestCase.class.getName() + ".steps", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestStep.class.getName(), jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestStep.class.getName() + ".testArtifacts", jcacheConfiguration);
            cm.createCache(eu.europa.ec.digit.bris.testbed.domain.TestArtifact.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
