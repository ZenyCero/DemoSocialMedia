package org.zerocool.securityservice.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zerocool.securityservice.domain.dto.UserKafka;
import org.zerocool.securityservice.domain.entity.Users;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreateEvent extends Event<UserKafka>{
}
