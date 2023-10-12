package com.api.project_management.utility;

public class ErrorResponse extends DataResponse<String>{
    @Override
    public void set(String data){
        this.status = "error";
        this.data = data;
    }
}
