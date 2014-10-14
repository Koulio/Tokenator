package org.tokenator.opentokenizer.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tokenator.opentokenizer.domain.entity.PrimaryData;

import java.util.Date;

public interface PrimaryDataRepository extends JpaRepository<PrimaryData, Long> {
    @Query(
            "FROM  PrimaryData AS pd " +
            "JOIN  pd.surrogates sd " +
            "WHERE sd.primaryData.id = pd.id " +
            "AND   sd.pan  = :surrogatePan " +
            "AND   sd.expr = :expr"
    )
    PrimaryData findBySurrogate(String surrogatePan, Date expr);

    PrimaryData findByPanAndExpr(String pan, Date expr);
}
