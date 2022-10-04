import  java.rmi.*;


class cliente{
 public static void main(String args[]){
  try{
  	  Hello serv = (Hello) Naming.lookup("rmi://127.0.0.1/ServidorHello");
      String retorno = serv.sayHello();
      System.out.println(retorno);
}catch(Exception e) {
	e.printStackTrace();
      }
	}
}