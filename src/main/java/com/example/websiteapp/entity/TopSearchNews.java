package com.example.websiteapp.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: sandy
 * \* Date: 2024/11/12
 * \* Time: 17:46
 * \* Description:
 * \
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "spider_top_search")

public class TopSearchNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sys_top_id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;
    @Column(name = "source")
    private String source;
    @Column(name = "date")
    private LocalDate date;

}