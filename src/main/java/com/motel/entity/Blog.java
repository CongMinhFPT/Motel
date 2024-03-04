package com.motel.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blogId;
    @Nationalized
    private String title;

    @Nationalized
    @Column(name = "descriptions", columnDefinition = "NVARCHAR(MAX)")
    private String descriptions;

    private String image;
    @Temporal(TemporalType.DATE)
    Date createDate = new Date();
    boolean status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagId")
    private Tag tag;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    private Account account;

    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(name = "blog", joinColumns = @JoinColumn(name = "blogId"), inverseJoinColumns = @JoinColumn(name = "tagId"))
    // private Set<Tag> tags = new HashSet<>();

}
