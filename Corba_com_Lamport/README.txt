Simulador de Sistema Distribuído com Consenso 

Disciplina: Sistemas Distribuídos
Professor(a): Tatiana Pereira Filgueiras 
Contato: tatiana.filgueiras@unifebe.edu.br

- Implementado em CORBA
- Utiliza o algoritmo de Lamport
- Utiliza cenário onde o Líder nunca é malicioso
- Ainda NÃO possui algoritmo de eleição de novo Lider

Testando:

- Inicie o ORB ( Start_ORBd.bat )
- Inicie o Líder ( Start_Lider.bat )
- Inicie os Tenentes ( Start_Tenente<id>.bat ), levando em conta 3f+1
- Inicie o Cliente ( Start_Client.bat )

Funcionamento:

 * Ao iniciar os tenentes, há a possibilidade de simular maliciosidade por 
   setá-la em 0 para não malicioso e em 1 para malicioso.
 * O Líder recebe a requisição do Cliente e repassa-a para os Tenentes
 * Os Tenentes repassam a requisição recebida para os demais Tenentes
     - Caso o Tenente seja malicioso, enviará uma requisição contrária a recebida
 * Ao receber 3f requisições, os Tenentes utilizam o algoritmo de consenso para 
   decidir pela requisição a ser processada
 * Ao decidir, os Tenentes enviam sua decisão ao Líder
 * Ao receber 3f decisões, o Líder utiliza o algoritmo de consenso para
   decidir qual requisição será processada e qual resposta será retornada
   ao cliente
 * O Líder responde ao Cliente

Licença de Uso:

 * GNU/GPL

 
