package org.zerocool.postservice.adapter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "medias")
public class Media {
    @Id
    private Long id;
    private Long postId;
    private String mediaType;
    private String fileId;
    private String fileName;
    private Long fileSize;
}
