package com.tab.AttendanceApp.util;
import com.tab.AttendanceApp.exception.ValidationException;
import com.tab.AttendanceApp.request.UserRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Validation {

  public void userValidation(UserRequest userRequest){

      Map<String,Object> error = new LinkedHashMap<>();

      if(ObjectUtils.isEmpty(userRequest)){

          throw  new IllegalArgumentException("user or json data is not present");
      }
      else{

          if(ObjectUtils.isEmpty(userRequest.getFirstName())){
              error.put("first-name","first-name is empty or null");
          }
          else{
              if(userRequest.getFirstName().length()<=3){
                  error.put("first-name","first-name at least 3 char");
              }
              if(userRequest.getFirstName().length()>=10){
                  error.put("first-name","first-name at most 10 char");
              }
          }

          if(ObjectUtils.isEmpty(userRequest.getLastName())){
              error.put("last-name","last-name is empty or null");
          }
          else{
              if(userRequest.getLastName().length()<=3){
                  error.put("last-name","last-name at least 3 char");
              }
              if(userRequest.getLastName().length()>=10){
                  error.put("last-name","last-name at most 10 char");
              }
          }

          if(ObjectUtils.isEmpty(userRequest.getEmail())){
              error.put("email","email is empty or null");
          }
          else{
              if(userRequest.getEmail().length()<=3){
                  error.put("email","email at least 3 char");
              }
              if(userRequest.getEmail().length()>=20){
                  error.put("email","email at most 20 char");
              }
          }

          if(ObjectUtils.isEmpty(userRequest.getUserRole())){
              error.put("userRole","userRole is empty or null");
          }
          else{
              if(!userRequest.getUserRole().equals("ADMIN") &&
                      !userRequest.getUserRole().equals("EMPLOYEE") &&
                      !userRequest.getUserRole().equals("MANAGER")){
                  error.put("userRole","userRole is INVALID");
              }

          }

          if (ObjectUtils.isEmpty(userRequest.getIsActive())) {
              error.put("isActive","isActive is empty or null");
          }
          else {
              if (userRequest.getIsActive() !=   Boolean.TRUE.booleanValue()
                      && userRequest.getIsActive() != Boolean.FALSE.booleanValue()) {
                  error.put("isActive", "isActive is INVALID");
              }
          }
      }
      if(!error.isEmpty()){
          throw new ValidationException(error);
      }
  }
}
