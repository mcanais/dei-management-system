# IMS: Exercício Prático BL5/2024

O **DEI Inventory Management System** (ou _IMS_) é uma plataforma interna do Departamento de Engenharia Informática (DEI) que permite a gestão centralizada de equipamentos e recursos utilizados diariamente pelos funcionários, professores e alunos do departamento.

De forma a facilitar o controlo e a manutenção do inventário do DEI, é necessário o desenvolvimento de um subsistema simples para este efeito: o **IMS: Inventory Management System** (o seu exercício).

Este subsistema deve permitir a gestão de:

- **Pessoas**, incluindo, para cada uma, pelo menos o nome, número de identificação, email, tipo (funcionário/professor/aluno) e outros campos que considere relevantes;
- **Recursos**, incluindo, para cada um, pelo menos o nome, ID, tipo (ex.: computadores, extensões, máquinas fotográficas), estado (disponível/em uso/em manutenção), localização e outros campos que considere relevantes;

Inicialmente, o **IMS** será utilizado apenas por funcionários dos Serviços do DEI, estando acessível apenas dentro da rede da Secretaria, não estando por isso previsto para o momento atual o desenvolvimento de autenticação.

## Implementação

O objetivo deste exercício é desenvolver o subsistema **IMS** usando Spring Boot[[1]](https://spring.io/projects/spring-boot) v2 (backend) e Vue.js[[2]](https://vuejs.org/) v3 (frontend).

O subsistema **IMS** deverá permitir, pelo menos:

- Visualizar todo o pessoal do DEI numa tabela de consulta rápida, contendo pelo menos nome e IST ID;
- Adicionar novo pessoal do DEI;
- Atualizar e remover pessoal do DEI existente;
- Atribuir uma pessoa a um recurso durante um período de tempo;
- Visualizar todas as interações de uma pessoa do DEI com os recursos do DEI, como quais recursos utilizou e quando;
- Visualizar todos os recursos numa tabela de consulta rápida, contendo pelo menos o nome, ID e estado;
- Adicionar novos recursos;
- Atualizar e remover recursos existentes;
- Filtrar recursos por tipo, estado ou outro campo relevante;
- Alterar o estado de um recurso para manutenção;
- Remover um recurso da manutenção;
- Consultar o histórico de utilização de um recurso, incluindo quem o utilizou e quando, e os períodos de manutenção;

É permitida (mas **não obrigatória**) a implementação de funcionalidades adicionais às acima, especialmente se considerar que algum aspeto melhoraria significativamente a _User Experience (UX)_ durante a utilização do sistema (por exemplo, uma homepage, um dashboard de estatísticas, envio de emails[[3]](https://github.com/tweedegolf/mailcrab), etc.).

É-lhe fornecido **código base** que deve analisar e utilizar como ponto de partida, estando algumas das funcionalidades acima já parcial ou integralmente implementadas. Não é necessária a implementação de testes. Será penalizada qualquer falta de validação de input do utilizador.

Encontra-se, na backend do código base, um exemplo de como poderá ser a estrutura das entidades a serem utilizadas. Este exemplo (**Materials**) não é relevante para o exercício, sendo apenas um exemplo de como estruturar camadas em Spring Boot caso não tenha experiência com este framework. Este exemplo deve ser removido e substituído pelas entidades relevantes para o exercício.

Alguns aspetos do código base poderão não estar de acordo com as melhores práticas de desenvolvimento, pelo que é sugerido que use o seu discernimento para melhorar o código existente, se assim o entender. Um exemplo de uma melhoria poderá ser criação de componentes reutilizáveis para código repetido.

Deve realizar o exercício de forma modular. Serão valorizadas qualidade e estética do código e da interface web apresentada.

Duvidas sobre os requisitos do exercício devem ser esclarecidas por email.

_Note-se que a situação descrita neste enunciado foi simplificada e não retrata necessariamente a realidade. A sua solução será usada apenas para fins de avaliação._

## Submissão

É obrigatório e alvo de avaliação a utilização de um sistema de controlo de versões (especificamente, `git`[\[4\]]) durante o desenvolvimento do exercício. Deve submeter a sua solução no repositório privado que lhe foi atribuído para o efeito, na instância de _GitLab_ da RNL[\[5\]], e que inicialmente contém o código base, do qual deve partir. Não é necessária a utilização de nenhuma funcionalidade específica do _GitLab_ (e.g., _Issues_ e _Merge Requests_), apenas da ferramenta `git` em si. Assim, ser-nos-á possível validar a sua capacidade de utilização desta ferramenta, podendo até ser-lhe benéfico durante a componente de entrevista.

Deve incluir, junto da sua solução, um ficheiro `README` que descreva o procedimento para iniciar um servidor local de testes. Pode incluir nesse ficheiro `README` qualquer informação que considere relevante para a avaliação do seu trabalho, tal como alguma eventual justificação de decisões tomadas. Não submeta artefactos irrelevantes (e.g., pasta `node_modules`).

**Prazo máximo de entrega: domingo, dia 10 de Janeiro de 2025, às 21h00 (hora de Lisboa).**

Não serão aceites quaisquer entregas após o prazo limite, por qualquer razão. Qualquer candidato que não entregue uma solução até ao prazo limite será automaticamente avaliado com a cotação de 0 (zero) valores na componente de seleção correspondente ao exercício prático.

**Boa sorte!**

## Recursos Potencialmente Úteis

- [https://vuetifyjs.com/en/](https://vuetifyjs.com/en/)
- [https://spring.io/guides](https://spring.io/guides)
- [https://www.javatpoint.com/spring-boot-architecture](https://www.javatpoint.com/spring-boot-architecture)

[[1]]: https://spring.io/projects/spring-boot

[[2]]: https://vuejs.org/

[[3]]: https://github.com/tweedegolf/mailcrab

[[4]]: https://git-scm.com/

[[5]]: https://gitlab.rnl.tecnico.ulisboa.pt/
