package ServerUtils;

import LamportCorba.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerDATA {

	private Map<String, org.omg.CORBA.Object> participantes = new HashMap<String, org.omg.CORBA.Object>();
	private ArrayList<Integer> decisoes = new ArrayList<Integer>(); //lista de decisoes para consenso
	private org.omg.CORBA.Object leaderLocation;
	private int requisicao;

	public ServerDATA(){
	}

    public ServerDATA(Map<String, org.omg.CORBA.Object> participantes,ArrayList<Integer> decisoes,int requisicao) {
    	this.participantes = participantes;
    	this.decisoes = decisoes;
    	this.requisicao = requisicao;
    }

	public void setLeaderLocation(org.omg.CORBA.Object leaderLocation){
		this.leaderLocation = leaderLocation;
	}

	public org.omg.CORBA.Object getLeaderLocation(){
		return this.leaderLocation;
	}

    public void addParticipante(org.omg.CORBA.Object servidor,String key){
    	this.participantes.put(key,servidor);
    }

    public void addDecisao(int decisao){
    	this.decisoes.add(decisao);
    }

    public void setParticipantes(Map<String, org.omg.CORBA.Object> participantes){
    	this.participantes = participantes;
    	System.out.println("Recebi lista de participantes de tamanho: "+this.participantes.size()+"\n");
    }

	public void clearDecisoes(){
		this.decisoes.clear();
	}

	public Map<String, org.omg.CORBA.Object> getParticipantes(){
		return this.participantes;
	}

	public ArrayList<Integer> getDecisoes(){
		return this.decisoes;
	}

}