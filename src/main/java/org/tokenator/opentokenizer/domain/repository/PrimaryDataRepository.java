package org.tokenator.opentokenizer.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tokenator.opentokenizer.domain.entity.PrimaryData;

import java.util.Date;

public interface PrimaryDataRepository extends JpaRepository<PrimaryData, Long> {
    @Query( "SELECT pd " +
            "FROM  PrimaryData pd " +
            "JOIN  pd.surrogates sd " +
            "WHERE sd.san  = :san " +
            "AND   sd.expr = :expr"
        )
    PrimaryData findBySurrogate(
            @Param("san") String san,
            @Param("expr") Date expr
    );

    PrimaryData findByPanAndExpr(String pan, Date expr);
}
