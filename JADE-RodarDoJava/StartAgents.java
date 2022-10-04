import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import java.io.Serializable;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class StartAgents extends Controller implements Serializable{

	protected static Runtime rt = null;
	protected static Profile p = null;
	protected static AgentContainer cc = null;

	public void Start()
	{
		//System.out.println("Startando.....");

		try
		{
			if (rt == null)
				rt = Runtime.instance();//inicia as configuracoes de inicio
			if (p == null)
				p = new ProfileImpl("agent.properties"); //propriedades do agente
			if (cc == null)
				cc = rt.createMainContainer(p); //MainContainer

			Object reference = (Object)this;
			Object args[] = new Object[1];
			args[0] = reference;
			AgentController jadeag;

			this.setContainer(cc);

			jadeag = cc.createNewAgent(this.getName(), "HOSTS", args);
			jadeag.start();
			//System.out.println("started: " + this.getName());


		}
		catch (Exception ex)
		{
			//System.out.println(ex.getMessage());
		}

	}

}
