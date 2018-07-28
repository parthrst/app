package com.vapps.uvpa;

import retrofit2.Call;
import retrofit2.http.GET;

 interface APIInterface

{



    @GET("/books/1")
    Call<Books> getBookName();

}

