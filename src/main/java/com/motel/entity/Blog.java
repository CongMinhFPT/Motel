package com.motel.entity;

import java.text.SimpleDateFormat;
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
import javax.persistence.Transient;

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

    Date createDate;

    boolean status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagId")
    private Tag tag;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    private Account account;

    @Transient
    public String getImagePath() {
        if (this.blogId == null)
            return "/admin/images/default-image.jpg";

        return "/upload/blog-files/" + this.blogId + "/" + this.image;
    }

    @Transient
    public String getShortDes() {
        if (descriptions.length() > 100) {
            return descriptions.substring(0, 100).concat("...");
        }
        return descriptions;
    }

    @Transient
    public String getCreateDateFormat() {
        if (createDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
            String strDate = formatter.format(createDate);
            return strDate;
        }
        return "";
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", title='" + title + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", image='" + image + '\'' +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }
}
