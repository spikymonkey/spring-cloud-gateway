/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.gateway.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cloud.gateway.route.RedisRouteDefinitionRepository;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GatewayRedisAutoConfigurationTests.Config.class,
		properties = "spring.cloud.gateway.redis-route-definition-repository.enabled=true")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
@ContextConfiguration(
		initializers = GatewayRedisRouteDefinitionRepositoryEnabledByPropertyTests.RedisDbInitializer.class)
public class GatewayRedisRouteDefinitionRepositoryEnabledByPropertyTests {

	@Container
	public static GenericContainer redis = new GenericContainer<>("redis:5.0.9-alpine").withExposedPorts(6379);

	@Autowired(required = false)
	private RedisRouteDefinitionRepository redisRouteDefinitionRepository;

	@BeforeAll
	public static void startRedisContainer() {
		redis.start();
	}

	@Test
	public void redisRouteDefinitionRepository() {
		assertThat(redisRouteDefinitionRepository).isNotNull();
	}

	public static class RedisDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues values = TestPropertyValues.of("spring.redis.port=" + redis.getFirstMappedPort());
			values.applyTo(configurableApplicationContext);
		}

	}

}
