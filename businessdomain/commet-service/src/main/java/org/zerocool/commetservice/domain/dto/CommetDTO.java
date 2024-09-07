package org.zerocool.commetservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommetDTO {
    private Long idPost;
    private Long idUser;
    private String content;
    private Date created;
    private Date updated;
}
