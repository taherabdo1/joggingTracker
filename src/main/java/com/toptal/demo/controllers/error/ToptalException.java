package com.toptal.demo.controllers.error;

import lombok.Getter;

@Getter
public class ToptalException extends Exception{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ToptalError toptalError;

    public ToptalException(ToptalError toptalError) {
        super(toptalError.getDescription());
        this.toptalError = toptalError;
    }
    

}
