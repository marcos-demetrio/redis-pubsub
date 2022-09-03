package com.example.redis;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriberService {

  @Autowired private ReactiveRedisOperations<String, String> redisOperations;

  @Value("${topic.name:joke-channel}")
  private String topic;

  @PostConstruct
  private void init() {
    this.redisOperations
        .listenTo(ChannelTopic.of(topic))
        .map(ReactiveSubscription.Message::getMessage)
        .subscribe(System.out::println);
  }
}
