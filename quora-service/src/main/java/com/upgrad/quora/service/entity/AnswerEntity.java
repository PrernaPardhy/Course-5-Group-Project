package com.upgrad.quora.service.entity;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name="answer")
@NamedQueries({
        @NamedQuery(name="answerUuid",query="select a from AnswerEntity a  where a.uuid =:uuid"),
        @NamedQuery(name="answerById", query="select a from AnswerEntity a inner join a.user u where u.id =:userid"),
        @NamedQuery(name = "answerByQuestionID",query= "select a from AnswerEntity a inner join a.question q where q.id = :questionid")

})

public class AnswerEntity implements Serializable {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="UUID")
    private String uuid;

    @Column(name="ANS")
    private String ans;

    @Column(name="DATE")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="QUESTION_ID")
    private QuestionEntity question;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

