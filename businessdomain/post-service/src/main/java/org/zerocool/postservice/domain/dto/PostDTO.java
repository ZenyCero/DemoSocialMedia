package org.zerocool.postservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long idUser;
    private String content;
    private Date created;
    private Date updated;
}
