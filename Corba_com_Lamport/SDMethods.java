package ServerUtils;

import LamportCorba.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SDMethods {

	private ServerUtils.ServerDATA data;
	private int jaDecidi=0,qtdMembros=0;

	public SDMethods(ServerUtils.ServerDATA data){
		this.data = data;
	}

	public String sayHello(){//se servidor nao for malicioso
         return "\nHello world !!\n";
    }

	public String sayBad(){  //se servidor for malicioso
		return "\nBad World For You !!\n";
	}
/*---------------------------------- SERVERS: repassam o que receberam do Lider para os demais membros ----------------------------------*/
	public void repassar(int maliciosidade,int jaDecidi,int requisicao,String myName,Map<String,org.omg.CORBA.Object> participantes){
		this.jaDecidi = jaDecidi;
		this.qtdMembros = (data.getParticipantes().size())-1;
		try{
	 		if(maliciosidade == 0){ //não é malicioso
				for(String key : participantes.keySet()){ //verifica os outros servidores
					try{
							if(!key.equalsIgnoreCase(myName) && !key.equalsIgnoreCase("Lider")){
								Servidor s = ServidorHelper.narrow(participantes.get(key)); //pega endereco de todos os tenentes
								System.out.println("Tentando enviar requisicao recebida para: " +key);

								/***************************************************************************************/
								s.mensagemDosMembros(requisicao); //envia a requisicao para todos os membros do SD
							   /***************************************************************************************/
							}
					}
					catch (Exception e) {
						System.err.println("Falha na conexão com tenentes\n");
					}
				}//for

					if(data.getDecisoes().size() == this.qtdMembros){ //se todos ja repassaram o que receberam
						if(this.jaDecidi == 0){
							decidir();//decide por qual foi a requisicao escolhida
							this.jaDecidi = 1;
							}
						}
				} //fim do maliciosidade = 0

			else { //se for malicioso

					for(String key : participantes.keySet()){ //verifica os outros servidores
						try{
							if(!key.equalsIgnoreCase(myName) && !key.equalsIgnoreCase("Lider")){
								Servidor s = ServidorHelper.narrow(participantes.get(key)); //Lider pega endereco de todos os tenentes
								System.out.println("Tenente malicioso tentando enviar operacoes divergentes. Membro:"+key);

								if(requisicao == 1){ //se a operacao recebida ==1
									s.mensagemDosMembros(2);//envia 2 >=)
									System.out.println("Requisicao=1, porem enviou 2");
								}else {
									s.mensagemDosMembros(1); //envia 1
									System.out.println("Requisicao=2, porem enviou 1");
								}
							}
						}catch (Exception e) {
	                   System.err.println("Falha ao enviar operacoes para: "+key);
						}
				}//for
						if(data.getDecisoes().size() == this.qtdMembros){ //se todos ja repassaram o que receberam
							if(this.jaDecidi == 0){
								decidir();//decide por qual foi a requisicao escolhida
								this.jaDecidi = 1;
							}
						}
			}//else
		}catch (Exception e) {
			System.err.println("Falha ao enviar operacoes\n");
		}
	}//repassar
/*---------------------------------- Servers: Decidem por uma Resposta e repassar ao Lider ----------------------------------*/
	public void decidir(){
		System.out.println("Decidindo....");
		respondeAoLider(new ServerUtils.SDUtils(data).findMaxValue());
	}

	private void respondeAoLider(int decisao_final){
		try{
			Servidor s = ServidorHelper.narrow(data.getLeaderLocation()); //localizando Lider
			System.out.println("Tentando enviar resposta final para o Lider...");
			/***************************************************************************************/
			s.mensagemDosMembros(decisao_final); //envia a resposta ao Lider
			/***************************************************************************************/
			System.out.println("Resposta Enviada!");
		}
		catch (Exception e) {
			System.err.println("Falha na conexão com o Lider\n");
		}
	}
/*---------------------------------- LIDER: Recebe uma requisicao do Cliente e Repassa aos demais Servers ----------------------------------*/
		public String processarRequest(int requisicao,Map<String,org.omg.CORBA.Object> participantes,String myName){ //somente se ele for lider

			for(String key : participantes.keySet()){ //verifica os outros servidores
				try{
					if(!key.equalsIgnoreCase(myName)){
						Servidor s = ServidorHelper.narrow(participantes.get(key)); //Lider pega endereco de todos os tenentes
						System.out.println("Tentando enviar requisicao recebida para: " +key);

						/***************************************************************************************/
						s.mensagemDoLider(requisicao); //envia a requisicao para todos os membros do SD
					   /***************************************************************************************/
					}
				}
			catch (Exception e) {
				System.err.println("Falha na conexão com tenentes\n");
				}
			}

			int qtdMembros = (data.getParticipantes().size()-1);
			while(data.getDecisoes().size()<qtdMembros) //enquanto todos ainda não tiverem decidido
			{
				//aguarda
				//verificar aperfeicoamento com listener ou OBSERVER
			}
			if(new ServerUtils.SDUtils(data).findMaxValue() == 1){ //se a maioria das respostas for a req 1
					data.clearDecisoes(); //limpa o array de decisoes
					return sayHello(); //retorna para o cliente a mensagem de boas vindas

			}else{
				    return sayBad();  //retorna "más vindas"
			}
		}//requisicao

}