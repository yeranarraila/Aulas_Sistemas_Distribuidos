import queueUtils.maIdent;

import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.net.InetAddress;
import java.net.UnknownHostException;


import jade.core.AID;

import jade.wrapper.ContainerController;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;


public class Controller implements java.io.Serializable{
	private maIdent leader=null;
	private maIdent m_reply=null;

	private AgentController ac;//para resumir/suspender um agente

	private String m_Sreply=null;

	private String m_name = null;
	private maIdent currentMA; // agente movel que esta utilizando o recurso

	protected RunHost ag;
	private int id=-1;
	private boolean isLocked=false;
	public boolean newView=false;
	private boolean changed = false;

	private final Lock l = new ReentrantLock();
	private ArrayList<String> nodos     = new ArrayList<String>();//lista de nodos interligados a este no
	private ArrayList<String> resources = new ArrayList<String>();//lista de recursos disponiveis neste nó

	private ArrayList<maIdent> queue     = new ArrayList<maIdent>(); //lista de agente moveis
	private ArrayList<maIdent> priorityQueue = new ArrayList<maIdent>(); //lista de agentes moveis preemptivos interrompidos
	private ArrayList<maIdent> preview_view  = new ArrayList<maIdent>(); //visao anterior
	private ArrayList<maIdent> current_view  = new ArrayList<maIdent>(); //visao atual

	private ArrayList<String> prioMAQueue    = new ArrayList <String>(); //prioridades dos agentes moveis da visão

	protected static ContainerController cc = null;

	public void Start()
	{
		return;
	}
	public void setCurrentMA(maIdent ma){
		currentMA = ma;
	}

	public maIdent getCurrentMA(){
		return currentMA;
	}

	public void setSuspended(AgentController ac){
		this.ac = ac;
	}

	public AgentController getSuspended(){
		return this.ac;
	}

	public void suspend(AgentController ac){
		try{
			ac.suspend();
		}catch(StaleProxyException e){
			e.printStackTrace();
		}
	}

	public void resume(AgentController ac){
		try{
			ac.activate();
		}catch(StaleProxyException e){
			e.printStackTrace();
		}

	}
	public void setChanged(boolean v){
		this.changed = v;
	}

	public boolean getChanged(){
		return this.changed;
	}

	public void setLocked(){
		l.lock();
	}

	public void setUnlocked(){
		l.unlock();
	}

	public void setContainer(ContainerController cc ){
		this.cc = cc;
	}

	public void updateStatus(boolean isLocked){
		this.isLocked = isLocked;
	}

	public boolean isLocked(){
		return isLocked;
	}
/************************************************/
/*           FUNCTIONS FOR VIEWS                */
/************************************************/

	public void setNewView(boolean v){
		this.newView = v;
	}

	public boolean getNewView(){
		return this.newView;
	}

	public boolean viewIsEmpty(String view){
		if(view.equals("preview_view")){
			return preview_view.isEmpty();
		}
		return current_view.isEmpty();
	}


	public void setPreviewView(ArrayList<maIdent> preview_view){
		this.preview_view = preview_view;
	}
	public int getPreviewViewSize(){
		return preview_view.size();
	}

	public maIdent getPVElement(int pos){
		return preview_view.get(pos);
	}
	public maIdent removePreviewViewElement(int pos){
		if(pos < preview_view.size()){
			 m_reply=  preview_view.remove(pos);

		}
		else{
			if(preview_view.size() == 1){
				 m_reply= preview_view.get(0);
				 preview_view.clear();
			}
		}
		return m_reply;
	}
	public void setLeader(maIdent leader){
		this.leader = leader;
	}

	public maIdent getLeader(){
		if(leader!= null) {
			return leader;
		}return new maIdent(new AID("NULL", AID.ISLOCALNAME));
	}

	public void chooseLeader(String view){
		if(view.equals("QUEUE")){
			this.leader = getElement     ((int)(Math.random()*(m_queueSize()))); //escolhe aleatoriamente um lider
		/*	while(this.leader.getName().startsWith("NULL")){
				this.leader = getElement     ((int)(Math.random()*(m_queueSize()))); //escolhe aleatoriamente um lider
			} */
		}else{
			this.leader = getViewElement ((int) (Math.random()*(m_viewSize()))); //escolhe aleatoriamente um lider
		/*	while(this.leader.getName().startsWith("NULL")){
				this.leader = getViewElement ((int) (Math.random()*(m_viewSize()))); //escolhe aleatoriamente um lider
			} */
		}
	}
/************************************************/
/*      FUNCTIONS FOR CURRENT_VIEW QUEUE        */
/************************************************/

	public boolean addToView(maIdent e){
		current_view.add(e);
		return true;
	}
	public maIdent removeViewElement(int pos){
		if(pos < m_viewSize()){
			 m_reply = current_view.remove(pos);

		}
		else{
			if(m_viewSize() == 1){
				 m_reply= current_view.get(0);
				 current_view.clear();
			}
		}
		return m_reply;
	}
	public maIdent getViewElement(int pos){
		return current_view.get(pos);
	}

	public int m_viewSize(){
		return current_view.size();
	}

	public void setCurrentView(ArrayList<maIdent> current_view){
		this.current_view = current_view;
	}
	public ArrayList<maIdent> getCurrentView(){
		return current_view;
	}
/************************************************/
/*           FUNCTIONS FOR SIMPLE QUEUE         */
/************************************************/

	public boolean addToQueue(maIdent e){
		queue.add(e);
		return true;
	}
	public void setInQueue(int index, maIdent element){
		queue.set(index,element);
	}
	public maIdent removeQueueElement(int pos){
		m_reply = null;
		if(pos < m_queueSize()){
			 m_reply= queue.remove(pos);
		}
		else{
			if(m_queueSize() == 1){
				 m_reply= queue.get(0);
				 queue.clear();
			}
		}
		return m_reply;
	}
	public maIdent getElement(int pos){
		return queue.get(pos);
	}
	public boolean queueIsEmpty(){
		return queue.isEmpty();
	}

	public int m_queueSize(){
		return queue.size();
	}

	public ArrayList<maIdent> getQueue(){
		return queue;
	}
/************************************************/
/*           FUNCTIONS FOR MAqueue QUEUE         */
/************************************************/

	public String removePRIOMAQueueElement(int pos){
		if(pos < m_PRIOMAqueueSize()){
			 m_Sreply= prioMAQueue.remove(pos);
		}
		else{
			if(m_PRIOMAqueueSize() == 1){
				 m_Sreply= prioMAQueue.get(0);
				 prioMAQueue.clear();
			}
		}
		return m_Sreply;
	}
	public String getPRIOMAElement(int pos){
		return prioMAQueue.get(pos);
	}
	public boolean PRIOMAqueueIsEmpty(){
		return prioMAQueue.isEmpty();
	}

	public void setPrioMaQueue(ArrayList<String> prioMAQueue){
		this.prioMAQueue = prioMAQueue;
	}
	public int m_PRIOMAqueueSize(){
		return prioMAQueue.size();
	}

	public ArrayList<String> getPRIOMAQueue(){
		return prioMAQueue;
	}

/************************************************/
/*           FUNCTIONS FOR PRIO QUEUE           */
/************************************************/
	public boolean addToPRIOQueue(maIdent e){
		priorityQueue.add(e);
		return true;
	}
	public maIdent removePRIOQueueElement(int pos){
		if(pos < m_PRIOqueueSize()){
			 m_reply= priorityQueue.remove(pos);
		}
		else{
			if(m_PRIOqueueSize() == 1){
				 m_reply= priorityQueue.get(0);
				 priorityQueue.clear();
			}
		}
		return m_reply;
	}
	public maIdent getPRIOElement(int pos){
		return priorityQueue.get(pos);
	}
	public boolean PRIOqueueIsEmpty(){
		return priorityQueue.isEmpty();
	}

	public int m_PRIOqueueSize(){
		return priorityQueue.size();
	}

/************************************************/
/*                                              */
/************************************************/
	public void setNodos(ArrayList nodos){
		this.nodos = nodos;
		//System.out.println("Controller: "+nodos.size()+" are setup.");
	}

	public ArrayList getNodos(){
		return this.nodos;
	}

	public void setResources(ArrayList resources){
		this.resources = resources;
		System.out.println("Controller: "+resources.size()+" resources are added.");
	}

	public ArrayList getResources(){
		return this.resources;
	}
	public ContainerController getContainer(){
		return this.cc;
	}

	private int getID(){
			try {
			String linha="";
	 	 	FileReader file = new FileReader( "HOSTS.config" );
         	Scanner in = new Scanner( file );
         	while (in.hasNext()) {
         		linha = in.next();

         		if(linha.contains("id")){
         			StringTokenizer st = new StringTokenizer(linha,"=");
         			while(st.hasMoreTokens()){
						linha = st.nextToken();
						if(linha.equals("id"))
							id = Integer.parseInt(st.nextToken());
            				//System.out.println("My id: "+id);
					}
         		}
         	}
           		in.close();
           		return id;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		return id;
	}
	public String getName() throws Exception
		{
			if (m_name == null)
				if(getID() == 0){
					m_name = new String("N" + getID());
				}else{
					m_name = InetAddress.getLocalHost().getHostName();
				}
			return m_name;
		}
	public int getMyID(){
			return this.id;
	}

	public void setStart(RunHost ag)
	{
		//System.out.println("Inicio setado!");
		this.ag = ag;
	}
	public Controller()
	{}
}