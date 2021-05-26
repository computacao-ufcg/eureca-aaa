## Criando um container Docker para deploy

<p>É necessário adicionar o seu user ao grupo de usuários do docker, usando o seguinte comando e depois reiniciar a máquina.</p>

<code>sudo usermod -aG docker $USER</code>

<p>Depois é só executar o <I>script</I> build_tag_push.sh:</p>

<code>bash build_tag_push.sh <git-branch> <docker-tag></code>

<p>Onde <git-branch> é o nome do branch que será usado para todos os repositórios e <docker-tag> é o rótulo
que será colocado na imagem construída e armazenada no Docker Hub.</p>

