package org.tokenator.opentokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.tokenator.opentokenizer.domain.entity.PrimaryData;
import org.tokenator.opentokenizer.domain.entity.SurrogateData;
import org.tokenator.opentokenizer.domain.repository.PrimaryDataRepository;
import org.tokenator.opentokenizer.util.DateSerializer_yyMM;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private PrimaryDataRepository primaryDataRepo;

    @Autowired
    public ApiController(PrimaryDataRepository primaryDataRepo) {
        this.primaryDataRepo = primaryDataRepo;
    }

    @RequestMapping(
            value = "/primaries",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PrimaryData createPrimary(@RequestBody PrimaryData primaryData) {
        return primaryDataRepo.save(primaryData);
    }

    @RequestMapping(
            value = "/primaries/{primaryId}",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PrimaryData findPrimaryById(@PathVariable(value="primaryId") Long primaryId) {
        return primaryDataRepo.findOne(primaryId);
    }

    @RequestMapping(
            value = "/primaries/{primaryPan}/{primaryExpr}",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PrimaryData findPrimaryByPanAndExpr(
            @PathVariable(value="primaryPan") String primaryPan,
            @PathVariable(value="primaryExpr") String primaryExpr) throws ParseException {
        return primaryDataRepo.findByPanAndExpr(primaryPan, DateSerializer_yyMM.convert(primaryExpr));
    }



    @RequestMapping(
            value = "/primaries/{primaryId}/surrogates/",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PrimaryData createSurrogate(
            @PathVariable(value="primaryId") Long primaryId,
            @RequestBody SurrogateData surrogateData
    ) throws ParseException {
        PrimaryData primary = primaryDataRepo.findOne(primaryId);
        if (primary != null) {
            primary.addSurrogate(surrogateData);
        }
        return primaryDataRepo.save(primary);
    }

    /*
    @RequestMapping(
            value = "/primaries/{primaryPan}/expr/{primaryExprYYMM}/surrogates/pan/{surrogatePan/expr/{surrogateExprYYMM}",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PrimaryData createSurrogate(
            @PathVariable(value="primaryDataId") Long primaryDataId,
            @PathVariable(value="surrogatePan") String pan,
            @PathVariable(value="exprYYMM") String exprYYMM) {
        PrimaryData primary = new PrimaryData(pan, convertExprStr(exprYYMM));
        return primaryDataRepo.save(primary);
    }
    */


    @RequestMapping(
            value = "/primaries/surrogates/{surrogatePan}/{surrogateExpr}",
            method = RequestMethod.GET
    )
    public PrimaryData findPrimaryOfSurrogate(
            @PathVariable(value="surrogatePan") String surrogatePan,
            @PathVariable(value="surrogateExpr") String surrogateExpr) throws ParseException {
        return primaryDataRepo.findBySurrogate(surrogatePan, DateSerializer_yyMM.convert(surrogateExpr));
    }

}
