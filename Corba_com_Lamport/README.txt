Simulador de Sistema Distribu�do com Consenso 

Disciplina: Sistemas Distribu�dos
Professor(a): Tatiana Pereira Filgueiras 
Contato: tatiana.filgueiras@unifebe.edu.br

- Implementado em CORBA
- Utiliza o algoritmo de Lamport
- Utiliza cen�rio onde o L�der nunca � malicioso
- Ainda N�O possui algoritmo de elei��o de novo Lider

Testando:

- Inicie o ORB ( Start_ORBd.bat )
- Inicie o L�der ( Start_Lider.bat )
- Inicie os Tenentes ( Start_Tenente<id>.bat ), levando em conta 3f+1
- Inicie o Cliente ( Start_Client.bat )

Funcionamento:

 * Ao iniciar os tenentes, h� a possibilidade de simular maliciosidade por 
   set�-la em 0 para n�o malicioso e em 1 para malicioso.
 * O L�der recebe a requisi��o do Cliente e repassa-a para os Tenentes
 * Os Tenentes repassam a requisi��o recebida para os demais Tenentes
     - Caso o Tenente seja malicioso, enviar� uma requisi��o contr�ria a recebida
 * Ao receber 3f requisi��es, os Tenentes utilizam o algoritmo de consenso para 
   decidir pela requisi��o a ser processada
 * Ao decidir, os Tenentes enviam sua decis�o ao L�der
 * Ao receber 3f decis�es, o L�der utiliza o algoritmo de consenso para
   decidir qual requisi��o ser� processada e qual resposta ser� retornada
   ao cliente
 * O L�der responde ao Cliente

Licen�a de Uso:

 * GNU/GPL

 
