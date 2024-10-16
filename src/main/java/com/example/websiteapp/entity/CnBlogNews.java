package com.example.websiteapp.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name= "spider-cnblognews")
public class CnBlogNews {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;
        @Column(name = "title")
        private String title;
        @Column(name="url")
        private String url;
        @Column(name="content")
        private String content;
        @Column(name="date")
        private Date date;

}
