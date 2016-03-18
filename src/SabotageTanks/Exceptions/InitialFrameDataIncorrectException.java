/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SabotageTanks.Exceptions;

import SabotageTanks.Interface.ShowMessage;

public class InitialFrameDataIncorrectException extends Exception
{
    public static final int PORT = 0;
    public static final int IP = 1;
    
    public InitialFrameDataIncorrectException(int field)
    {
        super();
        
        switch(field)
        {
            case PORT:
                ShowMessage.portIncorrect();
                break;
            case IP:
                ShowMessage.ipIncorrect();
                break;
        }
    }
}
