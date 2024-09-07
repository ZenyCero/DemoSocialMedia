package org.zerocool.commetservice.adapter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "commets")
public class Commet {
    @Id
    private Long id;
    private Long idPost;
    private Long idUser;
    private String content;
    private Date created;
    private Date updated;
}
