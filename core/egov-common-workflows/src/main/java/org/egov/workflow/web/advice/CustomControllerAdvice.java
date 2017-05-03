package org.egov.workflow.web.advice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.workflow.EgovWorkflowsApplication;
import org.egov.workflow.domain.exception.*;
import org.egov.workflow.web.contract.Error;
import org.egov.workflow.web.contract.ErrorResponse;
import org.egov.workflow.web.contract.FieldError;
import org.egov.workflow.web.contract.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParamsError(Exception ex) {
        return ex.getMessage();
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomBindException.class)
    public ErrorResponse handleBindingErrors(CustomBindException ex) {
        ErrorResponse errRes = ErrorResponse.builder().build();
    	BindingResult errors = ex.getErrors();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = Error.builder().build();
		if(errors.getGlobalError()!=null){
		error.setCode(errors.getGlobalError().getCode());
		error.setMessage(errors.getGlobalError().getObjectName());
		error.setDescription(errors.getGlobalError().getDefaultMessage());
		}else{
			if(errors.getFieldErrorCount()>0)
			error.setDescription("Missing fields");
		}
		if (errors.hasFieldErrors()) {
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
			for (org.springframework.validation.FieldError errs : fieldErrors) {
				FieldError f=new FieldError(errs.getField(),errs.getDefaultMessage());
				error.getFields().add(f);
				 
			}
		}
		errRes.setError(error);
		return errRes;
    }
    
    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public ErrorResponse handleBindingErrors(InvalidDataException ex) {
        ErrorResponse errRes = ErrorResponse.builder().build();
     
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
        Error error = Error.builder().code(InvalidDataException.code).message(ex.getFieldName()).description(ex.getMessage()).build();
		errRes.setError(error);
		
		return errRes;
    }
    
    
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoDataFoundException.class)
    public ErrorResponse handleBindingErrors(NoDataFoundException ex) {
        ErrorResponse errRes = ErrorResponse.builder().build();
     
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.OK.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = Error.builder().code(NoDataFoundException.code).message(ex.getMessage()).description(ex.getMessage()).build();
		errRes.setError(error);
		
		return errRes;
    }
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleThrowable(Exception ex) {
        ErrorResponse errRes = ErrorResponse.builder().build();
    	ex.printStackTrace(); 
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = Error.builder().code("Internal Server Error").message("Throwable").description(ex.getMessage()).build();

		return  errRes;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleServerError(Exception ex) {
    	ex.printStackTrace();
    	ErrorResponse errRes = ErrorResponse.builder().build();
    	 
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = Error.builder().build();
		 
		error.setCode("Internal Server Error");
		error.setMessage("500");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return  errRes;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ErrorResponse handleAuthenticationError(UnauthorizedAccessException ex) {
    	ex.printStackTrace();
        ErrorResponse errRes = ErrorResponse.builder().build();
   	 
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.UNAUTHORIZED.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = Error.builder().code("Un Authorized Access").message("404").description(ex.getMessage()).build();

		errRes.setError(error);
		return  errRes;
    }

   /* @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(InvalidComplaintStatusSearchException ex) {
        return new ComplaintStatusSearchErrorAdaptor().adapt(ex.getCriteria());
    }*/

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(InvalidComplaintStatusException ex) {
        Error error = Error.builder().message("Complaint status is invalid").build();
        return ErrorResponse.builder().error(error).build();
    }
  

}
