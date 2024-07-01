Aplicação Desafio Biblioteca API

Tecnologias utilizadas:
     -> IDE VSCode
     -> Java 21.0.02 (essencial para o funcionamento da aplicação)
     -> Postgres 16.3 (essencial para o funcionamento da aplicação)
     -> Insomnia (para testar as requisições http)

Tecnologias não utilizadas:
     -> Docker

Tecnologias não conhecidas:
     -> Swagger
     -> Deploy
     -> CI/CD
     -> Flyway

Dificuldades:
     -> Por ser a primeira vez que trabalho com APIs em Java, tive de pesquisar como usar o
     framework do Spring desde o mais básico

     -> Conexões ManyToMany são definitiviamente mais complexas que OneToMany e exisigiram
     muito mais estudo e testes para funcionar

     -> PutMappings, embora funcionem, são sucetiveis a bugs e inprevistos

     -> DeleteMappings, quando a entidade a ser deletada está sozinha ou até mesmo ligada a 
     um OneToMany, a exclusão fica simples, mas quando temos um ManyToMany envolvido, a coisa
     fica bem mais complicada

     -> Ligação à API externa, não consegui usar corretamente as bibliotecas de conexão do
     java às APIs da web, grande parte do conteúdo que achei estava descontinuado ou não
     se enquadrava no caso

     -> Usar datas em JSON, embora seja fácil declarar, utilizá-las pode ficar bem complexo
     e causar bugs facilmente