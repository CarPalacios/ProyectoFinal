package com.everis.topic;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Clase Topico.
 */
@Configuration
public class AccountTopic {

  @Value("${kafka.server.hostname}")
  private String hostName;

  @Value("${kafka.server.port}")
  private String port;

  /** Creacion del Topico. */
  @Bean
  public NewTopic createdAccountTopic() {

    return TopicBuilder
      .name("created-account-topic")
      .partitions(1)
      .replicas(1)
      .build();

  }

  /** Creacion del Topico. */
  @Bean
  public ProducerFactory<String, Object> producerFactory() {

    Map<String, Object> config = new HashMap<>();

    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hostName + ":" + port);

    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return new DefaultKafkaProducerFactory<>(config);

  }

  /** Creacion del Topico. */
  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {

    return new KafkaTemplate<>(producerFactory());

  }

}
