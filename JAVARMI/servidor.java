

import  java.rmi.*;
import java.rmi.server.*;
import java.net.*;
public class  servidor extends UnicastRemoteObject implements  Hello{
   public servidor() throws RemoteException{ super();  }
   public String sayHello() throws RemoteException{  // Método remoto
         return("Oi cliente");
   }
/* Continua */
 public static void main(String args[]) throws RemoteException{
   try{
    servidor serv= new servidor();
    Naming.rebind("ServidorHello",serv);   // Registra nome do servidor
    System.out.println("Servidor remoto pronto.");
   }
   catch(RemoteException e){  System.out.println("Exceção remota:"+e); }
   catch(MalformedURLException e){};
      }
}