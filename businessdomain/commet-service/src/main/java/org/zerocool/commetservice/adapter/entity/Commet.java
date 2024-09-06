package org.zerocool.commetservice.adapter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "commets")
public class Commet {
    private Long id;
    private Long idPost;
    private Long idUser;
    private String content;
    private Date created_at;
    private Date updated_at;
}
