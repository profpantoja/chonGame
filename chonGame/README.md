# chonGame
A JavaFX game for learning the main concepts from the object-oriented approach.
Teaching Units: <strong>Linguagens e Técnicas de Programação II</strong> e <strong>Introdução a Orientação a Objetos</Strong>.

## Como Contribuir Nesse Repositório:

- **Requisitos**: você precisa ser aluno ou monitor da disciplina ou, ainda, trabalhar em algum projeto de pesquisa.
- **Criar uma Branch**: após clonar o projeto em alguma IDE, é preciso criar uma branch baseada em algumas das categorias abaixo, de acordo com a funcionalidade desenvolvida.

| Category        | Meaning                                                                     |
| --------------- | --------------------------------------------------------------------------- |
| `wip`           | for a work in progress                                                      |
| `feature`       | for adding, removing or modifying a feature                                 |
| `experimental`  | for experimenting something which is not an issue                           |
| `hotfix`        | for quickly fixing critical issues, usually with a temporary solution Cell  |
| `bugfix`        | for fixing a bug                                                            |

> Exemplo de criação de uma branch para resolução de uma lista de exercícios:
  ```
  git checkout -b wip-primeiro-último-nome-projeto
  git checkout -b feature-primeiro-último_nome-feature-em-desenvolvimento
  ```

- **Adicionar os arquivos**: após a implementação das funcionalidades, adicione os arquivos alterados para prepará-los para confirmação e envio a este repositório.
> Exemplo de adição de todos os arquivos modificados:
  ```
  git add .
  ```

- **Realizar o commit localmente**: confirmar as modificações que precisam ser enviadas para serem avaliadas pelo responsável do repositório. Sempre adicione uma mensagem sobre o que foi realizado na confirmação.  
> Exemplo de um commit com uma mensagem:
  ```
  git commit -m "Uploading the new branch for the fixing a bug."
  ```

- **Efetuar o push**: envie suas modificações sempre que quiseres que tuas modificações sejam persistidas on-line no repositório e na branch criada anteriormente.
> Exemplo de como enviar as modificações confirmadas quando a branch ainda não existir no repositório:
  ```
  git push --set-upstream origin wip-exercícios-14-ao-20
  ```

> Exemplo de como enviar as modificações confirmadas para o repositório:
  ```
  git push
  ```

- **Solicitar mesclagem**: para que as modificações sejam refletidas no branch principal (`main`), é necessaŕio realizar um `pull request`. Esta solicitação é responsável por informar a outras pessoas sobre as alterações feitas em uma ramificação de um repositório. 
> Utilize o GitHub para realizar esta etapa.

## Instruções Úteis:

- **Criar uma branch a partir de uma outra**: se enventualmente modificações ocorreram em uma branch e é preciso levar essas modificações para uma outra branch.
> Exemplo de como criar a branch wip-nova-branch a partir da branch atual.
```
git checkout -b wip-nova-branch
```

