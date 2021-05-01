# eureca-as

## Enviando a aplicação para o DockerHub

- A partir da raiz do projeto, digite:

    `docker build -t eureca-as:dev .`

- Após o sucesso do build, esteja logado com sua conta do DockerHub para enviar a imagem.Para se conectar ao Docker:

    `docker login`

- Insira suas credenciais e faça o login.

**Criando a tag para a imagem.**

- Com a imagem montada, e o login efetuado, execute:

    `docker images`

- Recupere o id da imagem eureca-as, pois iremos utilizar no próximo passo.

    `docker tag "id_imagem" eureca/eureca-as:dev`

    `docker push eureca/eureca-as:dev`

- **"eureca/"** é o nome da organização que o docker enviará/atualizará a imagem.
- **eureca-as:dev** é o nome da imagem.