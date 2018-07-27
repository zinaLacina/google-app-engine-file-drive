/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.Date;

/**
 *
 * @author lacinazina
 */
public class Help {
    
    public boolean getRecent(Date date){
        Date today = new Date();
        return date.compareTo(today)==0;
    }
    
    public boolean noSpace(){
        return true;
    }
}
