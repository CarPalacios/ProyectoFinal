package com.everis.topic;

import com.everis.model.CreditConsumer;
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
public class CreditConsumerTopic {
  
  @Value("${spring.kafka.bootstrap-servers}")
  private String host;
  
  /** Creacion del Topico. */
  @Bean
  public NewTopic creditConsumerTransactionTopic() {
  
    return TopicBuilder
      .name("created-credit-consumer-topic")
      .partitions(1)
      .replicas(1)
      .build();
  
  }
  
  /** Creacion del Topico. */
  @Bean
  public ProducerFactory<String, CreditConsumer> producerFactory() {
  
    Map<String, Object> config = new HashMap<>();
    
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
    
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    
    return new DefaultKafkaProducerFactory<>(config);
  
  }
  
  /** Creacion del Topico. */
  @Bean  
  public KafkaTemplate<String, CreditConsumer> kafkaTemplate() {
  
    return new KafkaTemplate<>(producerFactory());
  
  }
  
}
