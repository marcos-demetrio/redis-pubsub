package com.example.redis;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RedisPublisherService {
  private static final String JOKE_API_ENDPOINT = "https://joke.deno.dev/";
  private WebClient webClient;

  @Autowired private ReactiveRedisTemplate<String, String> redisTemplate;

  @Value("${topic.name:joke-channel}")
  private String topic;

  @PostConstruct
  private void init() {
    this.webClient = WebClient.builder().baseUrl(JOKE_API_ENDPOINT).build();
  }

  @Scheduled(fixedRate = 3000)
  public void publish() {
    this.webClient
        .get()
        .retrieve()
        .bodyToMono(Joke.class)
        .flatMap(joke -> this.redisTemplate.convertAndSend(topic, joke.toString()))
        .subscribe();
  }
}
