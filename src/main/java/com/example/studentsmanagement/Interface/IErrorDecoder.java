package com.example.studentsmanagement.Interface;

import feign.Response;

public interface IErrorDecoder {
    Exception decode(String methodKey, Response response);
}
