# MyMarvelComics App
Esse aplicativo foi feito para te ajudar a salvar seus quadrinhos favoritos! :)

## Sumário
- [Arquitetura](#arquitetura)
- [System design](#system-design)
    - [Paginação](#paginação)
        - [Arquitetura e componentes](#arquitetura-e-componentes)
        - [CharactersRemoteMediator](#charactersremotemediator)
    - [Favoritos](#favoritos)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Core](#core)
- [Domain](#domain)
    - [Exemplo](#exemplo)
- [Design Patterns](#design-patterns)
    - [Princípio da inversão de dependência](#princípio-da-inversão-de-dependência)
    - [Command Pattern](#command-pattern)
- [Sobre componetização](#sobre-componetização)
    - [Como utilizar o LazyVerticalGridPaging?](#como-utilizar-o-lazyverticalgridpaging) 
- [Links](#links)
- [Screenshots](#screenshots)
- [Funcionalidades](#funcionalidades)
- [Build](#build)
- [Tecnologias Utilizadas](#tecnologias)
- [Outros tópicos para escrever sobre](#outros-tópicos-para-escrever-sobre)
## Arquitetura
**MyMarvelComicsApp** é baseado na arquitetura MVVM e o Repository pattern, seguindo [Guia de arquitetura docuemntada pelo google](https://developer.android.com/topic/)
Além disso, também incluo Clean Architecture.

<img width="960" alt="Canva Design" src="https://github.com/user-attachments/assets/509d5dff-d49c-4dad-ae70-e73026dab3f3">


# System design 

## Paginação

### Arquitetura e Componentes:

 - RemoteMediator: Interface fornecida pelo Paging do android para lidar com a intermediação entre a origem de dados remota e o cache local.
 - charactersDao: DAO (Data Access Object) para acessar e manipular o banco de dados local.
 - remoteService: Serviço para fazer requisições à API remota.
 - transactionProvider: Fornece uma maneira de executar transações no banco de dados, garantindo a integridade dos dados e evita a dependência do Appdabase.

### CharactersRemoteMediator

O CharactersRemoteMediator é uma classe que faz parte da implementação de paginação de dados usando o componente Paging 3 do Android Jetpack. O propósito dessa classe é gerenciar a carga e a atualização dos dados mais recentes do Web Server, e armazená-los localmente no nosso AppDatabasee. Ela lida com a lógica de paginação de 15 itens por vez a cada momento que o último foi alcançado. O uso do cache é importante para fornecer uma experiência de rolagem suave e eficiente para o usuário - além  um código limpo e manutenível. Além diss,o Lida com diferentes tipos de erros de forma robusta, fornecendo feedback apropriado. 

## Favoritos

O sistema de favoritação foi pensado para que os usuários possam armazenar os HQs em disco local. Na imagem abaixo, apresentamos uma visão geral do design do MyComicsApp. Anteriormente, ao discutir o sistema de paginação, mencionamos o DAO (Data Access Object). O DAO é uma interface responsável por abstrair as implementações de consultas em SQL, facilitando o acesso e a manipulação dos dados.
   
                                                    UI LAYER      DOMAIN LAYER          DATA LAYER            
             Em resumo: INTERAÇÃO DO USUÁRIO -> VIEW -> VIEWMODEL -> USECASE -> REPOSITORY -> DAO -> DATABASE  

<img src="https://developer.android.com/static/codelabs/basic-android-kotlin-compose-persisting-data-room/img/8b91b8bbd7256a63_1920.png?hl=pt-br">

# Estrutura do projeto
```        
├── Core
│   ├── Domain
│   ├── Data (repositories)
│   ├── Database (DAO, model/entities, converters)
│   ├── UI (commons, components)
|   ├── Models
│   ├── Network
├── Features
│   ├── Characters (contendo as screens e estrutura de navegação)
│   ├── Comics (contendo as screens e estrutura de navegação)
```
# Core

Contém os componentes centrais utilizados em todo o aplicativo.
  
# Domain

Contém as classes de domínio responsáveis para lidar com as regras de negócio do aplicativo. O ideal é deixar esse componente o mais isolado possível de frameworks externos. Desse modo, fica mais fácil de testar e escalar.

## Exemplo: 
A AddOrRemoveFromFavoritesImpl é responsável por verificar se o Comic marcado é favorito ou não para, a partir disso, decidir se vai adicionar ou deletar os item na tabela de favoritos - do nosso Appdatabase.
Nesse exemplo, nós podemos identificar 2 patterns importantes em desenvolvimento de sistemas: princípio da inversão de dependência e command pattern.
     
```
class AddOrRemoveFromFavoritesImpl(
    private val favoriteComics: ComicsRepository,
) : AddOrRemoveFromFavorites {
    override suspend fun invoke(
        comicId: Int,
        model: Comic,
        onResult: (ResultOf<Int>) -> Unit,
    ) {
        if (model.isFavorite) {
            favoriteComics.deleteFavoriteComic(comicId, onResult)
        } else {
            favoriteComics.addFavoriteComic(model, onResult)
        }
    }
}
```

# Design Patterns

- Atenção: para entender o contexto, você precisar ler o tópico anterior.

## O princípio da inversão de dependência

Esse princípio afirma que módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações - e abstrações não devem depender de detalhes. E estes devem depender de abstrações.
Em outras palavras, o princípio sugere que tanto as classes de alto nível (que contém a lógica de negócios mais importante) e as classes de baixo nível (que realizam tarefas mais específicas) devem depender de interfaces ou abstrações em vez de depender diretamente umas das outras. Nesse contexto, por exemplo, o AddOrRemoveFromFavoritesImpl depende de uma interface ComicsRepository que, por sua vez, depende de outra abstração ao invés de um objeto concreto. Com essa prática, o design do seu código se torna mais modular.

## Command Pattern

Padrão de design comportamental que encapsula uma solicitação como um objeto, permitindo que você parametrize métodos com diferentes solicitações, enfileire ou registre solicitações e suporte operações que podem ser desfeitas. Nesse contexto, a interface AddOrRemoveFromFavorites define a operação de adicionar/remover favoritos. A implementação decide a ação com base no estado do `comic` e chama métodos no ComicsRepository (o Receiver). Já o cliente, nosso ComicDetailsViewModel, pode atuar usando o Invoker que dispara a execução da operação.

### Benefícios: Permite adicionar novas operações sem modificar o código existente.    

#### Exemplo:

Anterioramente, O ComicsUseCase estava responsável apenas para chamar o fluxo de dados da paginação vindo da camada de dados. Os dados da paginação vêm puros do servidor (sem saber se é um comic favoritado ou não). Caso queiramos retornar uma lista de favoritos para mostrar itens misturados para o usuário (e com algum componente de visualizão, claro), basta nós acrescentarmos mais lógica dentro da implementação. Resultado: o retorno do contrato não muda ainda, apenas a implementação. Isso é muito flexível e fácil de escalar. 
 
 ```
class ComicsUseCaseImpl (private val comicsRepository: ComicsRepository):
    ComicsUseCase {
    override fun invoke(): Flow<PagingData<Comic>> {

       // this.client pede 15 itens por vez

        return comicsRepository.getPagingComics(15).flow.map {

         // fluxo de paginação

            it.map { comicEntity ->
                withContext(Dispatchers.IO) {
                    val call: suspend () -> Comic = {
                        async {
                            var resultComic: Comic? = null

                           // verifica com o repository se esse item é favorito ou não

                            comicsRepository.readFavoriteComic(comicEntity.id) {

                               // se for, retorna como favorito, caso contrário, segue em frente

                                resultComic = when (it) {
                                    is ResultOf.Success -> {
                                        it.value
                                    }
                                    is ResultOf.Failure -> {
                                        comicEntity.toComic()
                                    }
                                }
                            }
                            resultComic!!

                      // esperando pelo resultado: muito comum e útil para operações assíncronas

                        }.await()
                    }
                    call()
                }
            }
        }
    }
}
```

Ao longo do codebase você vai perceber o quanto fica fácil seguir esse design. Até mesmo para, caso ncessário, componentizar essas classes utilizando generics do kotlin.

# Sobre componetização
É muito comum ao longo do densenvolvimento de um software haver a necessidade de reutilizar abstrações.
Nesse projeto tem uma que gostei bastante, pois a criei pensando justamente para reutilizar na criação de telas semelhantes, uma prática muito comum para não somente economizarmos tempo, mas para testarmos.
No arquivo PagingComponents.kt você vai encontrar alguns exemplos de componentes que criei para reutilizar, como cards e listas.
E esse é meu favorito :
```        
@Composable
internal fun <T : Any> LazyVerticalGridPaging(
    modifier: Modifier,
    items: LazyPagingItems<T>,
    content: @Composable ColumnScope.(T?) -> Unit,
) {
    Box(modifier = modifier) {
        if (items.loadState.refresh is LoadState.Loading) {
            LoadingWithDeadPool(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyVerticalGrid(
                contentPadding = PaddingValues(8.dp),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize()
            ) {
                items(count = items.itemCount) { index ->
                    val item = items[index] as T?
                    CardScaffold(
                        Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .aspectRatio(0.8f)) {
                        content(item)
                    }
                }
            }
        }
        if (items.loadState.append is LoadState.Loading) {
            LoadingMoreItemsIndicator(Modifier.align(Alignment.BottomCenter))
        }
    }
}
```
Com o recurso de Generics do kotlin fica fácil de usar para qualquer tipo de dado, e é assim que faço para renderizar tanto a página de Comics quanto a de Characters

  ## Como utilizar o LazyVerticalGridPaging? 
     
    // aqui basta chamar a composable. No escopo que ela retorna você pode incluir qualquer tipo de composable. Você também pode usar o componente de Card criado e passar os parâmetros que nele contém
    // no contexto pra essa aplicação, os principais foram:
      - isFavorite
      - titulo
      - url da image 
     LazyVerticalGridPaging { item ->
         item.apply {
           CardContent(isFavorite, title, url)
         }
     }

# Links
 - [Banco de dados local no android](https://developer.android.com/training/data-storage/room?hl=pt-br)
 - [RemoteMediators](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db?hl=pt-br)

# Screenshots📱

| Listagem de HQ's                             | Detalhes de HQ                            | Carregando mais HQs               |
|----------------------------------------------|-------------------------------------------|-------------------------------------|
| <img src="https://github.com/user-attachments/assets/f13b8fd2-5f8b-4095-9091-956cdb858f95" alt="Alt text" style="width: 188px; height: 412px;"> | <img src="https://github.com/user-attachments/assets/66918b67-5193-4447-b572-2fe0b4c86235" alt="Alt text" style="width: 188px; height: 412px;">| <img src="https://github.com/user-attachments/assets/2335f483-3b6c-46fe-b91a-602b39949547" alt="Alt text" style="width: 188px; height: 412px;"> |

| Favoritando HQ                               | Excluindo HQ dos favoritos                | Sessão de favoritos               |
|----------------------------------------------|-------------------------------------------|-----------------------------------|
| <img src="https://github.com/user-attachments/assets/fa36f496-2a5b-4f19-ac35-95edfd1d3b9e" alt="Alt text" style="width: 188px; height: 412px;"> | <img src="https://github.com/user-attachments/assets/c0a85dfa-66cb-4845-9114-17a3c24d32a5" alt="Alt text" style="width: 188px; height: 412px;">| <img src="https://github.com/user-attachments/assets/c6ea4cdf-90c8-425e-987d-6937675f3fbe" alt="Alt text" style="width: 188px; height: 412px;"> |

| App Modal Drawer                             | Lista de Personagens                      | Detalhes do personagem              |
|----------------------------------------------|-------------------------------------------|-----------------------------------|
| <img src="https://github.com/user-attachments/assets/43cfc35c-79d3-4891-a998-90937f55b9c7" alt="Alt text" style="width: 188px; height: 412px;"> | <img src="https://github.com/user-attachments/assets/d4070244-1ec4-4146-a290-7f74a94c9e79" alt="Alt text" style="width: 188px; height: 412px;">| <img src="https://github.com/user-attachments/assets/ebcb83dc-c113-457e-aae5-ed1d5ab7f46b" alt="Alt text" style="width: 188px; height: 412px;">|


# Funcionalidades 🔬

-  Listagem de todos os personagens da marvel.
> 
-  Listagem de todos os HQ's da marvel.
> 
-  Salvar ou excluir HQ's como favorito.
> 
-  Listagem de HQ's por personagem selecionado.
>
-  Listagem de Personagens por HQ selecionado.

# Build

- [Registre uma chave](https://developer.marvel.com/)
- Você deve adicionar `PUBLIC_API_KEY` e a `PRIVATE_API_KEY` em seu `local.properties` para dar build na app, assim:

    ```kotlin
    PUBLIC_API_KEY=PUBLIC_API_KEY
    PRIVATE_API_KEY=PRIVATE_API_KEY
    ```

# Tecnologias 🛠️

Esse projeto utiliza:
* *Interceptors*
* *Robolectric*
* *MockWebserver*
* *Unit Tests*
* *Room*
* *Compose*
* *Dagger - Hilt*
* *SavedStateHandle*
* *Flow*
* *Coroutines*
* *Navigation*
* *ViewModel*
* *Material Design*
* *Retrofit*
* *Coil*
* *Kotlin*
* *OkhttpClient*
* *Pagging*

# Outros tópicos para escrever sobre

## Data: Repositórios responsáveis por buscar e armazenar dados.
## Database: Data Access Objects (DAOs) e entidades do banco de dados.
## UI: Componentes de interface do usuário comuns.
## Network: Configurações e chamadas de rede.
## Features: Contém as funcionalidades específicas do aplicativo.
## Characters: Contém as telas e a estrutura de navegação relacionadas aos personagens.
## Comics: Contém as telas e a estrutura de navegação relacionadas às HQs.
## O dagger-hilt é um facilitador para conteinerização da aplicação, ficando responsável por injetar e inicializar nossos componentes conforme nossos critérios.
