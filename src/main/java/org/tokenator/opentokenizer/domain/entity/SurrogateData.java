package org.tokenator.opentokenizer.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.ForeignKey;
import org.tokenator.opentokenizer.util.DateSerializer_yyMM;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "surrogate_data", indexes = {
        @Index(name = "sur_pan_ex_idx",  columnList="pan,expr", unique = true)
    }
)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SurrogateData {

    @Id
    @GeneratedValue
    private Long id;

    @Digits(integer = 19, fraction = 0)
    @Column(name = "pan", length=19, nullable = false)
    private String pan;

    @Temporal(TemporalType.DATE)
    @Column(name = "expr", nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyMM", timezone="UTC")
    @JsonSerialize(using = DateSerializer_yyMM.class)
    private Date expr;

    @ManyToOne
    @NotNull
    @JoinColumn(name="primary_data_id", nullable = false)
    @ForeignKey(name="fk_surrogate_primary")
    //@JsonBackReference
    private PrimaryData primaryData;

    public SurrogateData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Date getExpr() {
        return expr;
    }

    public void setExpr(Date expr) {
        this.expr = expr;
    }

    public PrimaryData getPrimaryData() {
        return primaryData;
    }

    public void setPrimaryData(PrimaryData primaryData) {
        this.primaryData = primaryData;
    }
}
