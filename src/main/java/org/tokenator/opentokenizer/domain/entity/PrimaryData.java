package org.tokenator.opentokenizer.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.tokenator.opentokenizer.util.DateSerializer;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.tokenator.opentokenizer.util.DateSerializer.DATE_FORMAT;

@Entity
@Table(name = "primary_data", indexes = {
        @Index(name = "pri_pan_ex_idx",  columnList="pan,expr", unique = true)
    }
)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonPropertyOrder({"id", "pan", "expr", "surrogates"})
public class PrimaryData {

    @Id
    @GeneratedValue
    private Long id;

    @Digits(integer = 19, fraction = 0)
    @Column(name = "pan", length=19, nullable = false)
    private String pan;

    @Temporal(TemporalType.DATE)
    @Column(name = "expr", nullable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=DATE_FORMAT, timezone="UTC")
    @JsonSerialize(using = DateSerializer.class)
    private Date expr;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="primaryData")
    //@JsonManagedReference
    private List<SurrogateData> surrogates = new ArrayList<>(0);

    public PrimaryData() {
    }

    public PrimaryData(String pan, Date expr) {
        this.pan = pan;
        this.expr = expr;
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

    public synchronized List<SurrogateData> getSurrogates() {
        return surrogates;
    }

    public synchronized void setSurrogates(List<SurrogateData> surrogates) {
        this.surrogates = surrogates;
    }

    public synchronized void addSurrogate(SurrogateData surrogateData) {
        if (surrogates.add(surrogateData)) {
            surrogateData.setPrimaryData(this);
        }
    }
}
