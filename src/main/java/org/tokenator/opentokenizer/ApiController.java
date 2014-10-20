package org.tokenator.opentokenizer;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.tokenator.opentokenizer.domain.entity.PrimaryData;
import org.tokenator.opentokenizer.domain.entity.SurrogateData;
import org.tokenator.opentokenizer.domain.repository.PrimaryDataRepository;
import org.tokenator.opentokenizer.domain.repository.SurrogateDataRepository;

import static org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit.LUHN_CHECK_DIGIT;

import javax.transaction.Transactional;
import java.util.Date;

import static org.tokenator.opentokenizer.util.DateSerializer.DATE_FORMAT;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private PrimaryDataRepository primaryDataRepo;
    private SurrogateDataRepository surrogateDataRepo;

    @Autowired
    public ApiController(
            PrimaryDataRepository primaryDataRepo,
            SurrogateDataRepository surrogateDataRepo
    ) {
        this.primaryDataRepo = primaryDataRepo;
        this.surrogateDataRepo = surrogateDataRepo;
    }


   /*
    *  Create a primary data entry.  Example:
    *
    *   $ curl -X POST -H 'Content-Type: application/json' -d '{"pan": "4046460664629718", "expr": "1801"}' \
    *       http://localhost:8080/api/v1/primaries/
    */
    @RequestMapping(
            value = "/primaries",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PrimaryData createPrimary(@RequestBody PrimaryData primaryData) {
        primaryData.setPan(validateAcctNumAndAdjustLuhn(primaryData.getPan()));
        return primaryDataRepo.save(primaryData);
    }


   /*
    *  Lookup primary PAN data by id. Example retrieving primary data for id=1:
    *
    *   $ curl -X GET http://localhost:8080/api/v1/primaries/1
    */
    @RequestMapping(
            value = "/primaries/{id}",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PrimaryData findPrimaryById(@PathVariable(value="id") Long id) {
        PrimaryData primary = primaryDataRepo.findOne(id);
        if (primary == null) {
            throw new EntityNotFoundException(PrimaryData.class, id);
        }
        return primary;
    }


    /*
     *  Lookup primary data by pan and yyMM expiration date.  Example:
     *
     *   $ curl -X GET http://localhost:8080/api/v1/primaries/4046460664629718/1801
     */
    @RequestMapping(
            value = "/primaries/{pan}/{expr}",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public PrimaryData findPrimaryByPanAndExpr(
            @PathVariable(value="pan") String pan,
            @PathVariable(value="expr")
            @DateTimeFormat(pattern = DATE_FORMAT) Date expr) {

        PrimaryData primary = primaryDataRepo.findByPanAndExpr(pan, expr);
        if (primary == null) {
            throw new EntityNotFoundException(PrimaryData.class, pan, expr);
        }
        return primary;
    }

    /*
     *  Delete primary entry by id.  Surrogates are deleted by cascade. Example:
     *
     *   $ curl -X DELETE http://localhost:8080/api/v1/primaries/1
     */
    @RequestMapping(
            value = "/primaries/{id}",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT) /* 204, success but no response payload */
    public void deletePrimaryById(@PathVariable(value="id") long primaryId) {
        primaryDataRepo.delete(primaryId);
    }



    /*
     *  Create a surrogate for a primary data entry with the specified id.  Example:
     *
     * $ curl -X POST -H 'Content-Type: application/json' -d '{"pan": "98765432109876", "expr": "1801"}' \
     *     http://localhost:8080/api/v1/primaries/1/surrogates/
     */
    @RequestMapping(
            value = "/primaries/{primaryId}/surrogates/",
            method = RequestMethod.POST
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public SurrogateData createSurrogate(
            @PathVariable(value="primaryId") Long primaryId,
            @RequestBody SurrogateData surrogateData) {

        PrimaryData primary = primaryDataRepo.findOne(primaryId);
        if (primary == null) {
            throw new EntityNotFoundException(PrimaryData.class, primaryId);
        }

        surrogateData.setSan(validateAcctNumAndAdjustLuhn(surrogateData.getSan()));
        primary.addSurrogate(surrogateData);

        return surrogateData;
    }


    /*
     *  Find the primary data that owns the requested surrogate pan+expr. Example:
     *
     *   $ curl -X GET http://localhost:8080/api/v1/primaries/surrogates/98765432109876/1801
     */
    @RequestMapping(
            value = "/primaries/surrogates/{san}/{expr}",
            method = RequestMethod.GET
    )
    @ResponseStatus(HttpStatus.OK)
    public PrimaryData findPrimaryOfSurrogate(
            @PathVariable(value="san") String san,
            @PathVariable(value="expr")
            @DateTimeFormat(pattern = DATE_FORMAT) Date expr) {
        PrimaryData primary = primaryDataRepo.findBySurrogate(san, expr);
        if (primary == null) {
            throw new EntityNotFoundException(SurrogateData.class, san, expr);
        }
        return primary;
    }


   /*
    *  Delete surrogate entry by id. Example
    *
    *   $ curl -X DELETE http://localhost:8080/api/v1/surrogates/1
    */
    @RequestMapping(
            value = "/surrogates/{surrogateId}",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT) /* 204, success but no response payload */
    @Transactional
    public void deleteSurrogate(@PathVariable(value="surrogateId") long surrogateId) {
        surrogateDataRepo.delete(surrogateId);
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND,reason="Entity not found")
    public void notFound() {
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value=HttpStatus.CONFLICT,reason="Entity already exists")
    public void duplicateEntryExists() {
    }


    /*
     *  If the account number ends in an 'X', the 'X' is replaced by a correct Luhn check
     *  digit, otherwise we verify the existing Luhn check digit.
     *
     *  Throws: InvalidPanException if the account number is invalid.
     */
    String validateAcctNumAndAdjustLuhn(String accountNumber) {
        int len = accountNumber.length();
        if (len < 12) {
            throw new InvalidAccountNumberException("PAN must be at least 12 digits");
        } else if (len > 19) {
            throw new InvalidAccountNumberException("PAN exceeds 19 digits");
        }

        char lastChar = accountNumber.charAt(len - 1);
        if (lastChar == 'x' || lastChar == 'X') {
            accountNumber = accountNumber.substring(0, len - 1);
            try {
                accountNumber += LUHN_CHECK_DIGIT.calculate(accountNumber);
            } catch (CheckDigitException e) {
                throw new InvalidAccountNumberException("Invalid PAN sequence");
            }
        } else {
            if (!LUHN_CHECK_DIGIT.isValid(accountNumber)) {
                throw new InvalidAccountNumberException("Luhn check failed, add an 'X' to end of account number for auto calculation");
            }
        }

        return accountNumber;
    }

}
