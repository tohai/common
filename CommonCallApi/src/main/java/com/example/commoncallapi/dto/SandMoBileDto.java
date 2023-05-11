package com.example.commoncallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SandMoBileDto {
      private String status = "SUCCESS";
      private String errorCode;
      private String errorDescription;
      private String signature;

      public boolean isSuccess(){
            return "SUCCESS".equalsIgnoreCase(this.status);
      }
}
