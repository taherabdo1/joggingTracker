package com.toptal.demo.controllers.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToptalErrorReponse {


    /** The description. */
    private String description;

    /** The error. */
    private String error;
    
    /** The status. */
    private String status;

}
