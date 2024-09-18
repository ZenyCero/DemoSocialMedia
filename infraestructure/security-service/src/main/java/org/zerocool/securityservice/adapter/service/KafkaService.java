package org.zerocool.securityservice.adapter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.zerocool.securityservice.domain.dto.UserKafka;
import org.zerocool.securityservice.domain.entity.Users;
import org.zerocool.securityservice.domain.event.Event;
import org.zerocool.securityservice.domain.event.EventType;
import org.zerocool.securityservice.domain.event.UserCreateEvent;
import org.zerocool.sharedlibrary.mapper.Mapper;

import java.util.Date;
import java.util.UUID;

@Component
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, Event<?>> producer;

    @Value("${topic.user.name:users}")
    private String topicUser;

    public void publish(Users users) {
        UserKafka user = Mapper.convertToOtherClass(users, UserKafka.class);
        UserCreateEvent created = new UserCreateEvent();
        created.setData(user);
        created.setId(UUID.randomUUID().toString());
        created.setType(EventType.CREATED);
        created.setDate(new Date());

        this.producer.send(topicUser, created);
    }
}
