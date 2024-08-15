# Criar Banco de dados Manualmente
## Passo 1: Configuração
Em `automanager/src/main/resources/application.properties` alterar para suas configurações pessoais:
1. `spring.datasource.username=root`
2. `spring.datasource.password=fatec`
```
#conexao
spring.datasource.url=jdbc:mysql://localhost:3306/base

spring.datasource.username=root <--- coloque o nome de usuário do seu banco MySQL
spring.datasource.password=fatec <--- e a sua respectiva senha

#comandos
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql: true

#logging.level.org.hibernate.SQL=debug
#logging.level.org.hibernate.type.descriptor.sql=trace
```

## Passo 2: Criando o Banco de Dados
- Abra o MySQL e faça login com suas credenciais.
- Clique em "File" no canto superior esquerdo e selecione "Open SQL Script".
- Navegue até a pasta onde o repositório foi clonado.
- Selecione o arquivo `base.sql` localizado diretamente na raiz da pasta clonada.
- Após abrir o arquivo, clique no primeiro ícone amarelo (o primeiro raio) para executar o script SQL e criar o banco de dados.

## Passo 3: Iniciar o Projeto
Inicie o projeto normalmente rodando o aqruivo que se encontra em:
`/automanager/src/main/java/com/autobots/automanager/AutomanagerApplication.java`




# Autobots
>[!NOTE]
>*Os exercícios se encontram nas branchs*

# Branchs 🔗
### [Atividade 1](https://github.com/miguelcondesantos/Autobots/tree/Atv1) <picture><source srcset="https://fonts.gstatic.com/s/e/notoemoji/latest/1f916/512.webp" type="image/webp"><img src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f916/512.gif" alt="🤖" width="32" height="32"></picture></br>
### [Atividade 2](https://github.com/miguelcondesantos/Autobots/tree/Atv2) <picture><source srcset="https://fonts.gstatic.com/s/e/notoemoji/latest/1f9bf/512.webp" type="image/webp"><img src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f9bf/512.gif" alt="🦿" width="32" height="32"></picture></br>
### [Atividade 3](https://github.com/miguelcondesantos/Autobots/tree/Atv3) <picture><source srcset="https://fonts.gstatic.com/s/e/notoemoji/latest/1f9be/512.webp" type="image/webp"><img src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f9be/512.gif" alt="🦾" width="32" height="32"></picture></br>
### [Atividade 4](https://github.com/miguelcondesantos/Autobots/tree/Atv4) <picture><source srcset="https://fonts.gstatic.com/s/e/notoemoji/latest/2699_fe0f/512.webp" type="image/webp"><img src="https://fonts.gstatic.com/s/e/notoemoji/latest/2699_fe0f/512.gif" alt="⚙" width="32" height="32"></picture></br>
### [Atividade 5](https://github.com/miguelcondesantos/Autobots/tree/Atv5) <picture><source srcset="https://fonts.gstatic.com/s/e/notoemoji/latest/1f680/512.webp" type="image/webp"><img src="https://fonts.gstatic.com/s/e/notoemoji/latest/1f680/512.gif" alt="🚀" width="32" height="32"></picture></br>
