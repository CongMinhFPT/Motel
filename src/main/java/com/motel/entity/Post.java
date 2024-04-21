package com.motel.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer postId;
    boolean status;
    @Temporal(TemporalType.DATE)
    @Column(name = "createDate")
    Date createDate = new Date();
    @Nationalized
    String title;

    @ManyToOne
    @JoinColumn(name = "accountId")
    @JsonBackReference
    Account account;

    @ManyToOne
    @JoinColumn(name = "motelRoomId")
    MotelRoom motelRoom;

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
        return "Post{" +
                "postId=" + postId +
                ", status=" + status +
                ", createDate=" + createDate +
                ", title='" + title +
                '}';
    }
}
