package com.iocl.lpg.customer.utils;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CustomerValidation implements Serializable{
    public CustomerValidation() {
        super();
    }

    public static int isValidOrNullVal(Object val, String expression) {
        if (val == null || StringUtils.isBlank(val.toString()))
        return EPICConstant.BLANK_CASE;
        Pattern reges = Pattern.compile(expression);
        Matcher m = reges.matcher(val.toString());
        Boolean regexValid = m.matches();
        return (regexValid == true ? EPICConstant.VALID_CASE : EPICConstant.INVALID_CASE);
    }

    public static int isValidOrNullVal(Object val) {
        if (val == null || StringUtils.isBlank(val.toString()))
            return EPICConstant.BLANK_CASE;

        return EPICConstant.VALID_CASE;
    }

    public static boolean isNull(Object val) {
        if (val == null || StringUtils.isBlank(val.toString()))
            return true;

        return false;
    }

    public static List validatePassword(String userName, String password) {
        List retValue = new ArrayList();
        retValue.add(0, "true");
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        
        if (password.length() < 8) {
            retValue.set(0, "false");
            retValue.add(1, CommonHelper.getValueFromRsBundle("SET_PWD_PWD_VALIDATION1"));
        } else if (password.indexOf(userName) > -1) {
            retValue.set(0, "false");
            retValue.add(1, CommonHelper.getValueFromRsBundle("SET_PWD_PWD_VALIDATION2"));
        } else if (!password.matches(upperCaseChars)) {
            retValue.set(0, "false");
            retValue.add(1, CommonHelper.getValueFromRsBundle("SET_PWD_PWD_VALIDATION3"));
        } else if (!password.matches(lowerCaseChars)) {
            retValue.set(0, "false");
            retValue.add(1, CommonHelper.getValueFromRsBundle("SET_PWD_PWD_VALIDATION4"));
        } else if (!password.matches(numbers)) {
            retValue.set(0, "false");
            retValue.add(1, CommonHelper.getValueFromRsBundle("SET_PWD_PWD_VALIDATION5"));
        } else if (!password.matches(specialChars)) {
            retValue.set(0, "false");
            retValue.add(1, CommonHelper.getValueFromRsBundle("SET_PWD_PWD_VALIDATION6"));
        }
        return retValue;
    }
    
    /**
     *Validation For POI Number 
     * @param number
     * @param poiType
     * @return
     */
    public static boolean validatePOI(String number,String poiType){
        boolean isPOIValid=true;
        if(poiType.equalsIgnoreCase("Pan Card")){
            isPOIValid=validatePANNumber(number);
        }else if(poiType.equalsIgnoreCase("Aadhaar(UID)")){
            isPOIValid=validateAadhaarNumber(number);
        }else if(poiType.equalsIgnoreCase("Passport")){
             isPOIValid=validatePassportNumber(number);
        }
        return isPOIValid;
    }
    
    public static boolean validatePANNumber(String panNumber) {
        //    String s = "ABCDE1234F"; // get your editext value here
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(panNumber);
        // Check if pattern matches
        if (matcher.matches()) {
            System.out.println("Matching" + "Yes");
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean validateAadhaarNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        Matcher matcher = aadharPattern.matcher(aadharNumber);
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;

    }
    public static boolean validatePassportNumber(String passportNumber) {
        Pattern passportPattern = Pattern.compile("[a-zA-Z]{1}\\d{7}");
        Matcher matcher = passportPattern.matcher(passportNumber);
        if (matcher.matches()) {
            System.out.println("Matching" + "Yes");
            return true;
        } else {
            return false;
        }

    }

}
